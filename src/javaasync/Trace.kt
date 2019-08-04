package javaasync

/**
 * Class for tracing code.
 */
object Trace {

    /*
     * Boolean variable checker if trace is on. By default is set to off
     * (false).
     */
    private var on = false

    /**
     * Turn on trace.
     */
    fun on() {
        on = true
    }

    /**
     * Turn off trace.
     */
    fun off() {
        on = false
    }

    /**
     * Print trace.
     *
     * @param string - string to print.
     */
    fun print(string: String) {
        if (on) {
            println(string)
        }
    }
}
