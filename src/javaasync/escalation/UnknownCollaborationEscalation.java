package javaasync.escalation;

import javaasync.Employee;

/**
 * Unknown collaboration escalation.
 */
public class UnknownCollaborationEscalation extends EscalationReport
{

    private final Employee sender;
    private final Employee receiver;

    /**
     * The constructor.
     *
     * @param sender - message sender.
     * @param receiver - message receiver.
     */
    public UnknownCollaborationEscalation(Employee sender, Employee receiver)
    {
        this.sender = sender;
        this.receiver = receiver;
    }

    /**
     * Handle unknown collaboration.
     */
    @Override
    public void handle()
    {
        if (sender.leavingJob() || receiver.leavingJob())
        {
            /*
             * Do nothing. It is not good to setup collaboration
             * if one of employees quit.
             */
        }
        else
        {
            sender.setupCollaboration(receiver);
        }

        recovered = true;
    }

    /**
     * Get reason in textual form.
     *
     * @return reason.
     */
    @Override
    public String reason()
    {
        return "Unkown collaboration between " + sender.getName() + " and "
            + receiver.getName()
            + ((recovered)
                ? " -> RECOVERED"
                : " -> NOT RECOVERED");
    }

}
