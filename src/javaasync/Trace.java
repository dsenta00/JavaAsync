package javaasync;

/**
 * Class for tracing code.
 */
public final class Trace
{

    /*
     * Boolean variable checker if trace is on. By default is set to off
     * (false).
     */
    private static boolean on = false;

    /**
     * Turn on trace.
     */
    public static void on()
    {
        on = true;
    }

    /**
     * Turn off trace.
     */
    public static void off()
    {
        on = false;
    }

    /**
     * Print trace.
     *
     * @param string - string to print.
     */
    public static void print(String string)
    {
        if (on)
        {
            System.out.println(string);
        }
    }
}
