package javaasync.escalation

import javaasync.Collaboration

import java.util.logging.Level
import java.util.logging.Logger

/**
 * Handle collaboration already exist.
 */
class CollaborationExistEscalation(private val collaboration: Collaboration) : EscalationReport() {

    override fun handle() {
        if (collaboration.closing()) {
            try {
                Thread.sleep(1)
            } catch (ex: InterruptedException) {
                Logger.getLogger(CollaborationExistEscalation::class.java.name).log(Level.SEVERE, null, ex)
            }

            collaboration.first().setupCollaboration(collaboration.second())
        }
    }

    override fun reason(): String {
        return ("Collaboration already exist between "
                + collaboration.first().name + " and "
                + collaboration.second().name
                + " TRYING TO RECOVER !")
    }
}
