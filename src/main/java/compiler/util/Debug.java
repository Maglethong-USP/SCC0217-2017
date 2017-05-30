package compiler.util;

/**
 * Class for debug logging
 *
 * <p/> To enable debug logging start program with parameter -D debug=true
 *
 * @author Maglethong Spirr
 */
public class Debug {

    private Debug() {}

    private static final boolean debug;

    static {
        debug = Boolean.parseBoolean(System.getProperty("debug"));
    }

    public static void log(String msg) {
        if (debug) {
            System.out.println(msg);
        }
    }
}
