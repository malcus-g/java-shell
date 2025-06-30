import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import utils.CommandsEnum;
import utils.Constants;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if(input.isEmpty())continue;

            String[] fullCommand = input.split(" ");
            String commandName = fullCommand[0];
            CommandsEnum command = CommandsEnum.fromString(commandName);

            if(command == null) {
                String path = CommandsEnum.findExecutableLocationInPath(commandName);
                if(!(path == null)) {
                    CommandsEnum.executeCommand(fullCommand, Paths.get("/").toAbsolutePath().toString());
                } else {
                    System.out.println(commandName + Constants.COMMAND_NOT_FOUND);
                }
                continue;
            }

            switch (command) {
                case EXIT:
                    System.exit(0);
                case ECHO:
                    CommandsEnum.handleEcho(fullCommand);
                    break;
                case TYPE:
                    CommandsEnum.handleType(fullCommand);
                    break;
            }
        }
    }
}
