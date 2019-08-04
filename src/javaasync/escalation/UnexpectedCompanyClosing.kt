package javaasync.escalation

import javaasync.Manager

/**
 * Unexpected Company Closing Escalation Report.
 */
class UnexpectedCompanyClosing(private val manager: Manager) : EscalationReport() {

    override fun handle() {
        /* TODO: Recover mechanism */
    }

    override fun reason(): String {
        return ""
    }

}
