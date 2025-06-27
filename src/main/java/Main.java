
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        String[] commandArray = {"exit", "echo", "type"};
        List<String> commandList = new ArrayList<>(Arrays.asList(commandArray));

        while(true){
            System.out.print("$ ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            String[] line = input.split(" ");
            String command = line[0];

            switch (command){
                case "exit":
                    return;
                case "echo":
                    StringBuilder text = new StringBuilder();
                    for (int i = 1; i < line.length; i++) {
                        text.append(line[i]).append(" ");
                    }
                    System.out.println(text);
                    break;
                case "type":
                    if(commandList.contains(line[1])){
                        System.out.println(line[1] + " is a shell builtin");
                    }else{
                        System.out.println(line[1] + ": not found");
                    }
                    break;
                default:
                    System.out.println(command + ": command not found");
            }
        }
    }
}
