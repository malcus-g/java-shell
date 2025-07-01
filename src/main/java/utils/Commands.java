package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public enum Commands {
    EXIT("exit"),
    ECHO("echo"),
    TYPE("type"),
    PWD("pwd"),
    CD("cd"),
    LS("ls");

    private final String value;

    Commands(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Commands fromString(String input) {
        for (Commands cmd : values()) {
            if (cmd.getValue().equals(input)) {
                return cmd;
            }
        }
        return null;
    }

    public static boolean isValidCommand(String input) {
        return fromString(input) != null;
    }

    public static void handleEcho(String[] line) {
        if (line.length > 1) {
            StringBuilder text = new StringBuilder();
            for (int i = 1; i < line.length; i++) {
                text.append(line[i]).append(" ");
            }
            System.out.println(text.toString().trim());
        } else {
            System.out.println();
        }
    }

    public static void handleType(String[] line) {
        if (line.length < 2) {
            System.out.println(line[0] + Constants.MISSING_OPERAND);
            return;
        }

        String query = line[1];
        if (isValidCommand(query)) {
            System.out.println(query + Constants.BUILTIN_SUFFIX);
            return;
        }

        try {
            String dir = findExecutableLocationInPath(query);
            if (!(dir == null)) {
                String output = Constants.isWindows ?
                        query + " is " + dir + query :
                        query + " is " + dir + File.separator + query;
                System.out.println(output);
            } else {
                System.out.println(query + Constants.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void handlePWD() {
        System.out.println(System.getProperty("user.dir"));
    }

    public static void handleCD(String[] command) {
        String separator = File.separator;
        String separatorRegex = separator.equals("\\") ? "\\\\" : separator;
        List<String> currentDir = new ArrayList<>(Arrays.asList(System.getProperty("user.dir").split(separatorRegex)));

        if (command.length > 1) {
            String path = command[1];

           path = path.replaceAll("[/\\\\]+", Matcher.quoteReplacement(separator));

            // Home directory
            if (path.startsWith("~")) {
                String homeDir = System.getProperty("user.home");
                path = path.replaceFirst("^~", Matcher.quoteReplacement(homeDir));
                System.setProperty("user.dir", path);
                return;
            }

            // Absolute paths
            if (path.startsWith(separator) || path.matches("(?i)^[a-z]:.*")) {
                Path absPath = Path.of(path);
                if (Files.exists(absPath)) {
                    System.setProperty("user.dir", absPath.toAbsolutePath().toString());
                } else {
                    System.out.println("cd: " + path + Constants.NO_SUCH_FILE);
                }
                return;
            }

            // Relative paths
            String[] argArray = path.split(Pattern.quote(separator));

            for (String file : argArray) {
                switch (file) {
                    case "", "." -> {
                    }
                    case ".." -> {
                        if (!currentDir.isEmpty()) {
                            currentDir.removeLast();
                        }
                    }
                    default -> currentDir.add(file);
                }
            }

            String newPath = String.join(separator, currentDir);
            Path newAbsPath = Path.of(newPath).toAbsolutePath();

            if (Files.exists(newAbsPath)) {
                System.setProperty("user.dir", newAbsPath.toString());
            } else {
                System.out.println("cd: " + path + Constants.NO_SUCH_FILE);
            }
        }
    }

    public static void handleLS() {
        String currentDir = System.getProperty("user.dir");
        try {
            Set<String> files = getDirectoryContents(currentDir);
            for (String file : files) {
                System.out.println(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Set<String> getDirectoryContents(String dir) throws IOException {
        Set<String> files;
        try (Stream<Path> stream = Files.list(Path.of(dir))) {
            files = stream
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new IOException(e);
        }
        return files;
    }

    public static Set<String> getDirectoryFiles(String dir) throws IOException {
        Set<String> files;
        try (Stream<Path> stream = Files.list(Path.of(dir))) {
            files = stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new IOException(e);
        }
        return files;
    }

    public static String findExecutableLocationInPath(String executable) throws IOException {
        String output = null;
        for (String path : Constants.ENV_PATH_LIST) {
            File tempFile = new File(path);
            Set<String> files;

            if (tempFile.exists()) {
                files = getDirectoryFiles(path);
            } else {
                continue;
            }

            for (String file : files) {
                Path absoluteFilePath = Constants.isWindows ?
                        Paths.get(path + file) :
                        Paths.get(path + File.separator + file);
                if (file.equals(executable) && Files.isExecutable(absoluteFilePath)) {
                    output = path;
                    break;
                }
            }
        }
        return output;
    }

    public static void executeCommand(String[] command, String path) {

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File(path));
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();


        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
