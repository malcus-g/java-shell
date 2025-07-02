import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Constants {
    // Responses
    public static final String COMMAND_NOT_FOUND = ": command not found";
    public static final String NOT_FOUND = ": not found";
    public static final String BUILTIN_SUFFIX = " is a shell builtin";
    public static final String MISSING_OPERAND = ": missing operand";
    public static final String NO_SUCH_FILE = ": No such file or directory";

    // Environment
    public static final boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    public static final String ENV_PATH = System.getenv("PATH");
    public static final List<String> ENV_PATH_LIST = Arrays.asList(ENV_PATH.split(File.pathSeparator));
}
