package javaasync.escalation

import javaasync.Manager
import javaasync.Secretary

import java.util.ArrayList

/**
 * Escalation manager tries to solve company escalation.
 */
class EscalationManager(manager: Manager) {

    /*
     * Escalation history.
     */
    private val escalationHistory: MutableList<EscalationReport> = ArrayList()

    /*
     * Shared secretary 3:)
     */
    private val secretary: Secretary = manager.secretary

    /**
     * Handle report.
     *
     * @param report - escalation report.
     */
    fun handle(report: EscalationReport) {
        secretary.error("\u001B[31m" + report.reason() + "\u001B[0m")
        report.handle()
        escalationHistory.add(report)
    }
}
