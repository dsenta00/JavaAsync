package javaasync.escalation;

import java.util.ArrayList;
import java.util.List;
import javaasync.Manager;
import javaasync.Secretary;

/**
 * Escalation manager tries to solve company escalation.
 */
public class EscalationManager
{

    /*
     * Escalation history.
     */
    private final List<EscalationReport> escalationHistory;

    /*
     * Line manager.
     */
    private final Manager manager;

    /*
     * Shared secretary 3:)
     */
    private final Secretary secretary;

    /**
     * The constructor.
     *
     * @param manager - the manager.
     */
    public EscalationManager(Manager manager)
    {
        escalationHistory = new ArrayList<>();
        this.manager = manager;
        this.secretary = manager.getSecretary();
    }

    /**
     * Handle report.
     *
     * @param report - escalation report.
     */
    public void handle(EscalationReport report)
    {
        secretary.error("\u001B[31m" + report.reason() + "\u001B[0m");
        report.handle();
        escalationHistory.add(report);
    }
}
