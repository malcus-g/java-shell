package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public enum CommandsEnum {
    EXIT("exit"),
    ECHO("echo"),
    TYPE("type");

    private final String value;

    CommandsEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CommandsEnum fromString(String input) {
        for (CommandsEnum cmd : values()) {
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
        if(line.length > 1) {
            StringBuilder text = new StringBuilder();
            for (int i = 1; i < line.length; i++) {
                text.append(line[i]).append(" ");
            }
            System.out.println(text.toString().trim());
        }else {
            System.out.println();
        }
    }

    public static void handleType(String[] line) {
        if(line.length < 2) {
            System.out.println(line[0] + Constants.MISSING_OPERAND);
            return;
        }

        String query = line[1];
        if(isValidCommand(query)) {
            System.out.println(query + Constants.BUILTIN_SUFFIX);
            return;
        }

        try {
            String dir = findExecutableLocationInPath(query);
            if(!(dir == null)) {
                System.out.println(query + " is " + dir + "/" + query);
            }else {
                System.out.println(query + Constants.NOT_FOUND);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Set<String> getFiles(String dir) throws Exception {
        Set<String> files = new HashSet<>();
        try (Stream<Path> stream = Files.list(Path.of(dir))) {
            files = stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }catch(Exception e){
            throw new Exception(e);
        }
        return files;
    }

    public static String findExecutableLocationInPath(String executable) {
        String output = null;
        for(String path : Constants.ENV_PATH_LIST) {
            Set<String> files = null;
            try {
                files = getFiles(path);
            } catch (Exception e) {
                continue;
            }
            for(String file : files){
                if(file.equals(executable)){
                    output = path;
                    break;
                }
            }
            if(output != null){
                break;
            }
        }
        return output;
    }
}
