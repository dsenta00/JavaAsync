package javaasync;

/**
 * Message for confirm to Employee that Collaboration will close.
 */
public class CloseCollaborationConfirm extends Message
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
