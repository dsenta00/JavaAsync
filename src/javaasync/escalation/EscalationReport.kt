package javaasync.escalation

/**
 * Escalation report.
 */
abstract class EscalationReport {

    /*
     * Flag to see if recovered.
     */
    protected var recovered = false

    /**
     * Handle escalation.
     */
    abstract fun handle()

    /**
     * Get reason.
     *
     * @return reason in textual form.
     */
    abstract fun reason(): String
}
