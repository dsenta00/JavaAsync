package javamessagingnetbeans;

/**
 * Dummy message for notifying another employees for job quit.
 */
public class CloseCollaborationRequest extends Message
{

    public CloseCollaborationRequest(Employee sender)
    {
        super(sender);
    }
}
