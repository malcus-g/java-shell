import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import utils.CommandsEnum;
import utils.Constants;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        for(String path : Constants.ENV_PATH_LIST){
            System.out.println(path);
        }

        while(true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if(input.isEmpty())continue;

            String[] line = input.split(" ");
            String commandName = line[0];
            CommandsEnum command = CommandsEnum.fromString(commandName);

            if(command == null) {
                System.out.println(commandName + Constants.COMMAND_NOT_FOUND);
                continue;
            }

            switch (command) {
                case EXIT:
                    System.exit(0);
                case ECHO:
                    CommandsEnum.handleEcho(line);
                    break;
                case TYPE:
                    CommandsEnum.handleType(line);
                    break;
            }
        }
    }
}
