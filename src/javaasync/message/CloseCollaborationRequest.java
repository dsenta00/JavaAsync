package javaasync.message;

import javaasync.Employee;

/**
 * Dummy message for notifying another employees for job quit.
 */
public class CloseCollaborationRequest extends CompanyMessage
{

    /**
     * The Constructor.
     *
     * @param sender - Employee sender.
     */
    public CloseCollaborationRequest(Employee sender)
    {
        super(sender);
    }
}
