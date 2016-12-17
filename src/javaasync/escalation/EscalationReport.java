package javaasync.escalation;

/**
 * Escalation report.
 */
public abstract class EscalationReport
{

    /*
     * Flag to see if recovered.
     */
    protected boolean recovered = false;

    /**
     * Handle escalation.
     */
    public abstract void handle();

    /**
     * Get reason.
     *
     * @return reason in textual form.
     */
    public abstract String reason();
}
