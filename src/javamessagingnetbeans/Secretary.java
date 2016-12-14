package javamessagingnetbeans;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lady that does everything administrative.
 */
public class Secretary
{

    /*
     * File name where traces are collected.
     */
    protected String fileName;

    /*
     * Type writer machine.
     */
    protected PrintWriter typingMachine;

    /**
     * Setup her workplace.
     *
     * @param fileName - log file name.
     */
    public Secretary(String fileName)
    {
        this.fileName = fileName;

        try
        {
            this.typingMachine = new PrintWriter(this.fileName, "UTF-8");
        }
        catch (FileNotFoundException | UnsupportedEncodingException ex)
        {
            Logger.getLogger(Secretary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Logging part that executes from Thread.
     *
     * @param info - information to store into typing machine.
     */
    protected void logThread(String info)
    {
        String format = "[DEBUG] "
            + new SimpleDateFormat("[HH:mm:ss.SSS] ").format(new Date())
            + info;

        this.typingMachine.println(format);

        Trace.print(format);
    }

    /**
     * Log information.
     *
     * @param info - The information.
     */
    public void log(final String info)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Secretary.this.logThread(info);
            }
        });

        thread.start();
    }

    /**
     * Give her lay-off pay and tell her to return the typing machine.
     */
    public void giveLayOffPay()
    {
        this.typingMachine.close();
    }
}
