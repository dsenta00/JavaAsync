package javaasync.escalation;

import javaasync.Manager;

/**
 * Unexpected Company Closing Escalation Report.
 */
public class UnexpectedCompanyClosing extends EscalationReport
{

    private final Manager manager;

    /**
     * The constructor.
     *
     * @param manager - the manager.
     */
    public UnexpectedCompanyClosing(Manager manager)
    {
        this.manager = manager;
    }

    @Override
    public void handle()
    {
        /* TODO: Recover mechanism */
    }

    @Override
    public String reason()
    {
        return "";
    }

}
