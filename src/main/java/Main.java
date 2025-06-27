
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

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
                default:
                    System.out.println(command + ": command not found");
            }
        }
    }
}
