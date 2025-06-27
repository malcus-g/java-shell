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
