package javaasync.escalation;

import java.util.logging.Level;
import java.util.logging.Logger;
import javaasync.Collaboration;

/**
 * Handle collaboration already exist.
 */
public class CollaborationExistEscalation extends EscalationReport
{

    /*
     * Existing collaboration.
     */
    private final Collaboration collaboration;

    /**
     * The constructor.
     *
     * @param collaboration - collaboration.
     */
    public CollaborationExistEscalation(Collaboration collaboration)
    {
        this.collaboration = collaboration;
    }

    @Override
    public void handle()
    {
        if (collaboration.closing())
        {
            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(CollaborationExistEscalation.class.getName()).log(Level.SEVERE, null, ex);
            }

            collaboration.first().setupCollaboration(collaboration.second());
        }
    }

    @Override
    public String reason()
    {
        return "Collaboration already exist between "
            + collaboration.first().getName() + " and "
            + collaboration.second().getName()
            + " TRYING TO RECOVER !";
    }

}
