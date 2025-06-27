package utils;

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

    public static boolean isValid(String input) {
        return fromString(input) != null;
    }
}
