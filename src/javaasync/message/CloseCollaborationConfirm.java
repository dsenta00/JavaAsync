package javaasync.message;

import javaasync.Employee;

/**
 * Message for confirm to Employee that Collaboration will close.
 */
public class CloseCollaborationConfirm extends CompanyMessage
{

    /**
     * The constructor.
     *
     * @param sender - Employee sender.
     */
    public CloseCollaborationConfirm(Employee sender)
    {
        super(sender);
    }
}
