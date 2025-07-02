import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if(input.isEmpty())continue;

            String[] fullCommand = input.split(" ");
            String commandName = fullCommand[0];
            Commands command = Commands.fromString(commandName);

            if(command == null) {
                String path;

                try{
                    path = Utils.findExecutableLocationInPath(commandName);

                    if(!(path == null)) {
                        Utils.executeCommand(fullCommand, Paths.get(System.getProperty("user.home")).toAbsolutePath().toString());
                    } else {
                        System.out.println(commandName + Constants.COMMAND_NOT_FOUND);
                    }
                } catch(RuntimeException e) {
                    System.out.println(e.getMessage());
                } finally {
                    continue;
                }
            }

            switch (command) {
                case EXIT:
                    System.exit(0);
                case ECHO:
                    Commands.handleEcho(fullCommand);
                    break;
                case TYPE:
                    Commands.handleType(fullCommand);
                    break;
                case PWD:
                    Commands.handlePWD();
                    break;
                case CD:
                    Commands.handleCD(fullCommand);
                    break;
                case LS:
                    Commands.handleLS();
                    break;
            }

        }
    }
}
