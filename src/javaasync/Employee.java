package javaasync;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaasync.escalation.CollaborationExistEscalation;
import javaasync.escalation.EscalationReport;
import javaasync.escalation.UnknownCollaborationEscalation;

/**
 * Employee class represents a independent worker. Each employee is ran through
 * its own thread. Employees can collaborate in order to exchange messages.
 */
public abstract class Employee extends Thread
{

    /*
     * Every employee has rights for a vacation.
     */
    EmployeesVacation vacation;

    /*
     * Employee name.
     */
    private String employeeName;

    /*
     * Collaboration map.
     */
    private final Map<Employee, Collaboration> collaborationMap;

    /*
     * Employees own task schedule.
     */
    private Collaboration ownTasks;

    /*
     * Main manager.
     */
    private Manager manager;

    /*
     * Flag to check if employee is leaving job.
     */
    private boolean leavingJob;

    /**
     * Constructor.
     */
    public Employee()
    {
        manager = null;
        employeeName = null;
        collaborationMap = new ConcurrentHashMap<>();
        vacation = new EmployeesVacation();
        leavingJob = false;
    }

    /**
     * Leave company.
     */
    private void leaveCompany()
    {
        log("just left company");
        Thread.currentThread().stop();
    }

    /**
     * Handle message CloseCollaborationRequest.
     *
     * @param message - CloseCollaborationRequest message
     */
    private void handleMessageCloseCollaborationRequest(Message message)
    {
        Collaboration collaboration = getCollaboration(message.sender());

        if (collaboration == null)
        {
            escalation(new UnknownCollaborationEscalation(message.sender(), this));
            return;
        }

        log("closing collaboration with " + message.sender().name());

        collaboration.send(this, new CloseCollaborationConfirm(this));
        collaborationMap.remove(message.sender());
    }

    /**
     * Handle message CloseCollaborationConfirm.
     *
     * @param message - CloseCollaborationConfirm message
     */
    private void handleMessageCloseCollaborationConfirm(Message message)
    {
        collaborationMap.remove(message.sender());

        log("closed collaboration with " + message.sender().name());

        if (collaborationMap.isEmpty())
        {
            /*
             * All collaborations are closed.
             * Leave company.
             */
            leaveCompany();
        }
    }

    /**
     * Setup collaboration with employee.
     *
     * @param employee - employee to set collaboration with.
     * @param collaboration - collaboration contract to set employee.
     */
    private void offerCollaboration(Employee employee, Collaboration collaboration)
    {
        Collaboration existingCollaboration = getCollaboration(employee);

        if (existingCollaboration != null)
        {
            escalation(new CollaborationExistEscalation(existingCollaboration));
            return;
        }

        log("offered collaboration from " + employee.name());

        collaborationMap.put(employee, collaboration);
    }

    /**
     * Get employees manager.
     *
     * @return The Manager.
     */
    protected Manager manager()
    {
        return manager;
    }

    /**
     * Create new employee.
     *
     * @param <T> - type extends Employee class.
     * @param clazz - employee type.
     * @param name - employee name.
     *
     * @return new employee.
     */
    protected <T extends Employee> T createEmployee(Class<T> clazz, String name)
    {
        return getEmployee(name) == null
            ? manager.createEmployee(clazz, name)
            : null;
    }

    /**
     * Setup collaboration with employee.
     *
     * @param employee - employee to set collaboration with.
     */
    public void setupCollaboration(Employee employee)
    {
        Collaboration collaboration = getCollaboration(employee);

        if (collaboration != null)
        {
            escalation(new CollaborationExistEscalation(collaboration));
            return;
        }

        log("setup collaboration with " + employee.name());

        collaboration = new Collaboration(this, employee);

        collaborationMap.put(employee, collaboration);
        employee.offerCollaboration(this, collaboration);
    }

    /**
     * Get collaboration between another employee.
     *
     * @param employee - another employee.
     * @return Collaboration if exist, otherwise null.
     */
    protected Collaboration getCollaboration(Employee employee)
    {
        return collaborationMap.get(employee);
    }

    /**
     * Get employee by his name.
     *
     * @param name - employees name.
     * @return Employee if exist, otherwise null.
     */
    protected Employee getEmployee(String name)
    {
        return manager.getEmployee(name);
    }

    /**
     * Send message if collaboration between employees exist. Otherwise throw
     * escalation.
     *
     * @param <T> - message type.
     * @param message - message to send.
     * @param toEmployee - employee to send message.
     */
    protected <T> void send(T message, Employee toEmployee)
    {
        Collaboration collaboration = getCollaboration(toEmployee);

        if (collaboration == null)
        {
            if (this != toEmployee)
            {
                escalation(new UnknownCollaborationEscalation(this, toEmployee));
                return;
            }
            else
            {
                collaboration = ownTasks;
            }
        }

        collaboration.send(this, new Message(this, message));
    }

    /**
     * Send with timeout.
     *
     * @param <T> - message type.
     * @param message - message.
     * @param toEmployee - to employee.
     * @param tmo - timeout in milliseconds.
     */
    protected <T> void send(final T message, final Employee toEmployee, final int tmo)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(tmo);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
                }

                Employee.this.send(message, toEmployee);
            }
        });

        thread.start();
    }

    /**
     * Send message to all employees.
     *
     * @param <T> - message type.
     * @param message - message to send.
     */
    protected <T> void sendToAll(T message)
    {
        for (Collaboration collaboration : collaborationMap.values())
        {
            collaboration.send(this, new Message(this, message));
        }
    }

    /**
     * Check if employee is leaving company.
     *
     * @return true if leaving, otherwise false.
     */
    public boolean leavingJob()
    {
        return leavingJob;
    }

    /**
     * Send all collaboration request in order to quit job. If no collaboration
     * exist, leave company.
     */
    public void quitJob()
    {
        log("leaving company");

        leavingJob = true;

        if (collaborationMap.isEmpty())
        {
            leaveCompany();
            return;
        }

        for (Collaboration collaboration : collaborationMap.values())
        {
            collaboration.send(this, new CloseCollaborationRequest(this));
        }
    }

    /**
     * Throw a escalation.
     *
     * @param report - escalation report.
     */
    protected void escalation(EscalationReport report)
    {
        manager.raiseEscalation(report);
    }

    /**
     * Relax for some time.
     *
     * @param nanoseconds - milliseconds to sleep.
     */
    protected void relax(int nanoseconds)
    {
        try
        {
            Thread.sleep(0, nanoseconds);
        }
        catch (InterruptedException e)
        {
        }
    }

    /**
     * Printout employee log.
     *
     * @param string - string to print
     */
    protected void log(String string)
    {
        manager.getSecretary().log(name() + " -> " + string);
    }

    /**
     * Receive message.
     *
     * @return the Message.
     */
    protected Message receive()
    {
        for (;;)
        {
            Message message = null;

            for (Collaboration collaboration : collaborationMap.values())
            {
                message = collaboration.receiveMessage(this);

                if (message != null)
                {
                    break;
                }
            }

            /*
             * If not received messages from others, check own tasks.
             */
            if (message == null)
            {
                message = ownTasks.receiveMessage(this);
            }

            if (message == null)
            {
                int relaxTime = vacation.count();

                if (relaxTime > 0)
                {
                    relax(relaxTime);
                }
                else if (relaxTime == -1)
                {
                    manager.fireEmployee(employeeName);
                }
            }
            else
            {
                vacation.reset();

                if (message instanceof CloseCollaborationRequest)
                {
                    handleMessageCloseCollaborationRequest(message);
                }
                else if (message instanceof CloseCollaborationConfirm)
                {
                    handleMessageCloseCollaborationConfirm(message);
                }
                else if (!leavingJob)
                {
                    return message;
                }
            }
        }
    }

    /**
     * Set employee name.
     *
     * @param name - name.
     */
    public void name(String name)
    {
        employeeName = name;
    }

    /**
     * Get employee name.
     *
     * @return - employee name.
     */
    public String name()
    {
        return employeeName;
    }

    /**
     * Set manager.
     *
     * @param manager - manager.
     */
    public void manager(Manager manager)
    {
        this.manager = manager;
    }

    /**
     * Initialize employee.
     */
    public abstract void init();

    /**
     * Message event handler.
     *
     * @param message - received message.
     */
    public abstract void messageEvent(Message message);

    /**
     * Executable part of Thread.
     */
    @Override
    public void run()
    {
        ownTasks = new Collaboration(this, this);

        init();

        for (;;)
        {
            messageEvent(receive());
        }
    }
}
