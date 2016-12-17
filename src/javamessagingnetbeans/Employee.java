package javamessagingnetbeans;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
            exception("Collaboration with employee "
                + message.sender().name() + " doesn't exist.");
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
        if (getCollaboration(employee) != null)
        {
            exception("collaboration already exist!");
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
    protected void setupCollaboration(Employee employee)
    {
        if (getCollaboration(employee) != null)
        {
            exception("collaboration with employee already exist!");
            return;
        }

        log("setup collaboration with " + employee.name());

        Collaboration newCollaboration = new Collaboration(this, employee);

        collaborationMap.put(employee, newCollaboration);
        employee.offerCollaboration(this, newCollaboration);
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
     * exception.
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
            exception("Collaboration doesn't exist!");
            return;
        }

        collaboration.send(this, new Message(this, message));
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
     * Throw a exception.
     *
     * @param message - the detail message.
     */
    protected void exception(String message)
    {
        try
        {
            throw new Exception(message);
        }
        catch (Exception e)
        {
        }
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
        init();

        for (;;)
        {
            messageEvent(receive());
        }
    }
}
