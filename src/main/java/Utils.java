import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

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

