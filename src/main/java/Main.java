import java.util.Scanner;

import utils.CommandsEnum;
import utils.Constants;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.print("$ ");
            String input = scanner.nextLine();
            if(input.isEmpty())continue;

            String[] line = input.split(" ");
            String commandName = line[0];
            CommandsEnum command = CommandsEnum.fromString(commandName);

            if(command == null){
                System.out.println(commandName + Constants.COMMAND_NOT_FOUND);
                continue;
            }

            switch (command){
                case EXIT:
                    System.exit(0);
                case ECHO:
                    handleEcho(line);
                    break;
                case TYPE:
                    handleType(line);
                    break;
            }
        }
    }

    public static void handleEcho(String[] line){
        if(line.length > 1){
            StringBuilder text = new StringBuilder();
            for (int i = 1; i < line.length; i++) {
                text.append(line[i]).append(" ");
            }
            System.out.println(text.toString().trim());
        }else{
            System.out.println();
        }
    }

    public static void handleType(String[] line){
        if(line.length < 2){
            System.out.println(line[0] + Constants.MISSING_OPERAND);
            return;
        }

        String query = line[1];
        if(CommandsEnum.isValid(query)){
            System.out.println(query + Constants.BUILTIN_SUFFIX);
        }else{
            System.out.println(query + Constants.NOT_FOUND);
        }
    }
}
