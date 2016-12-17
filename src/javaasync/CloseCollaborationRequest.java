package javaasync;

/**
 * Dummy message for notifying another employees for job quit.
 */
public class CloseCollaborationRequest extends Message
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
