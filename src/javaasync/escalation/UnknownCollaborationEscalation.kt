package javaasync.escalation

import javaasync.AsyncService

/**
 * Unknown collaboration escalation.
 */
class UnknownCollaborationEscalation(
    private val sender: AsyncService,
    private val receiver: AsyncService
) : EscalationReport() {

    /**
     * Handle unknown collaboration.
     */
    override fun handle() {
        if (sender.leavingJob() || receiver.leavingJob()) {
            /*
             * Do nothing. It is not good to setup collaboration
             * if one of employees quit.
             */
        } else {
            sender.setupCollaboration(receiver)
        }

        recovered = true
    }

    /**
     * Get reason in textual form.
     *
     * @return reason.
     */
    override fun reason(): String {
        return ("Unkown collaboration between " + sender.name + " and "
                + receiver.name
                + if (recovered)
            " -> RECOVERED"
        else
            " -> NOT RECOVERED")
    }

}
