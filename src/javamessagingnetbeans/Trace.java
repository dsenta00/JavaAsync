package javamessagingnetbeans;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for tracing code.
 */
public final class Trace
{
    /*
     * Boolean variable checker if trace is on. By default is set to off
     * (false).
     */
    private static boolean traceOn = false;

    /**
     * Turn on trace.
     */
    public static void traceOn()
    {
        traceOn = true;
    }

    /**
     * Turn off trace.
     */
    public static void traceOff()
    {
        traceOn = false;
    }

    /**
     * Print trace.
     *
     * @param string - string to print.
     */
    public static void print(String string)
    {
        if (traceOn)
        {
            System.out.println(new SimpleDateFormat("[HH:mm:ss.SSS] ").format(
                    new Date()).toString()
                    + string);
        }
    }
}
