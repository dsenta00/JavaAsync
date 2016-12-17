package javaasync;

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

    /*
     * Check if secretary is on.
     */
    private boolean on;

    /**
     * Setup her workplace.
     *
     * @param fileName - log file name.
     */
    public Secretary(String fileName)
    {
        this.fileName = fileName;
        on = false;

        try
        {
            typingMachine = new PrintWriter(fileName, "UTF-8");
        }
        catch (FileNotFoundException | UnsupportedEncodingException ex)
        {
            Logger.getLogger(Secretary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Logging part that executes from Thread.
     *
     * @param logType - log type
     * @param info - information to store into typing machine.
     */
    protected void logThread(String logType, String info)
    {
        String format = "[" + logType.toUpperCase() + "] "
            + new SimpleDateFormat("[HH:mm:ss.SSS] ").format(new Date())
            + info;

        if (on)
        {
            typingMachine.println(format);
        }

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
                Secretary.this.logThread("DEBUG", info);
            }
        });

        thread.start();
    }

    /**
     * Log error information.
     *
     * @param info - The information.
     */
    public void error(final String info)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Secretary.this.logThread("ERROR", info);
            }
        });

        thread.start();
    }

    /**
     * Give her lay-off pay and tell her to return the typing machine.
     */
    public void giveLayOffPay()
    {
        typingMachine.close();
    }

    /**
     * Turn secretary on :)
     */
    void on()
    {
        on = true;
    }

    /**
     * Turn secretary off :(
     */
    void off()
    {
        on = false;
    }
}
