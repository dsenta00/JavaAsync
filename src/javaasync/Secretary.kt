package javaasync

import java.io.FileNotFoundException
import java.io.PrintWriter
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Lady that does everything administrative.
 */
class Secretary(private var fileName: String) {

    /*
     * Type writer machine.
     */
    private lateinit var typingMachine: PrintWriter

    /*
     * Check if secretary is on.
     */
    private var on: Boolean = false

    init {
        on = false

        try {
            typingMachine = PrintWriter(fileName, "UTF-8")
        } catch (ex: FileNotFoundException) {
            Logger.getLogger(Secretary::class.java.name).log(Level.SEVERE, null, ex)
        } catch (ex: UnsupportedEncodingException) {
            Logger.getLogger(Secretary::class.java.name).log(Level.SEVERE, null, ex)
        }
    }

    /**
     * Logging part that executes from Thread.
     *
     * @param logType - log type
     * @param info    - information to store into typing machine.
     */
    protected fun logThread(logType: String, info: String) {
        val format = ("[" + logType.toUpperCase() + "] "
                + SimpleDateFormat("[HH:mm:ss.SSS] ").format(Date())
                + info)

        if (on) {
            typingMachine.println(format)
        }

        Trace.print(format)
    }

    /**
     * Log information.
     *
     * @param info - The information.
     */
    fun log(info: String) {
        val thread = Thread(Runnable { this@Secretary.logThread("DEBUG", info) })

        thread.start()
    }

    /**
     * Log error information.
     *
     * @param info - The information.
     */
    fun error(info: String) {
        val thread = Thread(Runnable { this@Secretary.logThread("ERROR", info) })

        thread.start()
    }

    /**
     * Give her lay-off pay and tell her to return the typing machine.
     */
    fun giveLayOffPay() {
        typingMachine.close()
    }

    /**
     * Turn secretary on :)
     */
    internal fun on() {
        on = true
    }

    /**
     * Turn secretary off :(
     */
    internal fun off() {
        on = false
    }
}
