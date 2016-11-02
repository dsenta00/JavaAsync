package javamessagingnetbeans;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Employee class represents a independent worker. Each employee is ran through
 * its own thread. Employee can collaborate with another employees in order to
 * exchange messages.
 */
public abstract class Employee extends Thread
{
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

    /**
     * Constructor.
     */
    public Employee() {
        this.manager = null;
        this.employeeName = null;
        this.collaborationMap = new ConcurrentHashMap<>();
    }

    /**
     * Leave company.
     */
    private void leaveCompany()
    {
        this.trace("just left company");

        this.manager.removeEmployee(this);
        Thread.currentThread().stop();
    }

    /**
     * Handle message CloseCollaborationRequest.
     *
     * @param message - CloseCollaborationRequest message
     */
    private void handleMessageCloseCollaborationRequest(Message message)
    {
        Collaboration collaborationToClose = this
                .getCollaboration(message.sender());

        if (collaborationToClose == null)
        {
            this.exception("Collaboration with employee "
                    + message.sender().getEmployeeName() + " doesn't exist.");
            return;
        }

        this.trace("closing collaboration with "
                + message.sender().getEmployeeName());

        collaborationToClose.cleanQueue();
        this.sendMessage(new CloseCollaborationConfirm(), message.sender());
        this.collaborationMap.remove(message.sender());
    }

    /**
     * Handle message CloseCollaborationConfirm.
     *
     * @param message - CloseCollaborationConfirm message
     */
    private void handleMessageCloseCollaborationConfirm(Message message)
    {
        this.collaborationMap.remove(message.sender());

        this.trace("closed collaboration with "
                + message.sender().getEmployeeName());

        if (this.collaborationMap.isEmpty())
        {
            this.leaveCompany();
        }
    }

    /**
     * Setup collaboration with employee.
     *
     * @param employee - employee to set collaboration with.
     * @param collaboration - collaboration contract to set employee.
     */
    private void offerCollaboration(Employee employee,
            Collaboration collaboration)
    {
        if (this.getCollaboration(employee) != null)
        {
            this.exception("collaboration with employee already exist!");
            return;
        }

        this.trace("offered collaboration from " + employee.getEmployeeName());

        this.collaborationMap.put(employee, collaboration);
    }

    /**
     * Get employees manager.
     *
     * @return The Manager.
     */
    protected Manager getManager()
    {
        return this.manager;
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
        return this.manager.createEmployee(clazz, name);
    }

    /**
     * Setup collaboration with employee.
     *
     * @param employee - employee to set collaboration with.
     */
    protected void setupCollaboration(Employee employee)
    {
        if (this.getCollaboration(employee) != null)
        {
            this.exception("collaboration with employee already exist!");
            return;
        }

        this.trace("setup collaboration with " + employee.getEmployeeName());

        Collaboration newCollaboration = new Collaboration(this, employee);

        this.collaborationMap.put(employee, newCollaboration);
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
        return this.collaborationMap.get(employee);
    }

    /**
     * Get employee by his name.
     *
     * @param name - employees name.
     * @return Employee if exist, otherwise null.
     */
    protected Employee getEmployee(String name)
    {
        return this.manager.getEmployee(name);
    }

    /**
     * Send message if collaboration between employees exist. Otherwise throw
     * exception.
     *
     * @param <T> - message type.
     * @param message - message to send.
     * @param toEmployee - employee to send message.
     */
    protected <T> void sendMessage(T message, Employee toEmployee)
    {
        Collaboration collaboration = this.getCollaboration(toEmployee);

        if (collaboration == null)
        {
            this.exception(
                    "Collaboration between two employees doesn't exist!");
            return;
        }

        collaboration.sendMessage(this, new Message(this, message));
    }

    /**
     * Send all collaboration request in order to quit job.
     */
    public void quitJob()
    {
        this.trace("leaving company");

        if (this.collaborationMap.isEmpty())
        {
            this.leaveCompany();
            return;
        }

        for (Entry<Employee, Collaboration> entry : this.collaborationMap
                .entrySet())
        {
            entry.getValue().cleanQueue();
            this.sendMessage(new CloseCollaborationRequest(), entry.getKey());
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
     * @param milliseconds - milliseconds to sleep.
     */
    protected void relax(long milliseconds)
    {
        this.trace("relax for " + milliseconds + " milliseconds");

        try
        {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException e)
        {
        }
    }

    /**
     * Printout employee trace.
     *
     * @param string - string to print
     */
    protected void trace(String string)
    {
        Trace.print(this.getEmployeeName() + " => " + string);
    }

    /**
     * Receive message.
     *
     * @return the Message.
     */
    protected Message receiveMessage()
    {
        for (;;)
        {
            Message message = null;

            for (Collaboration collaboration : this.collaborationMap.values())
            {
                message = collaboration.receiveMessage(this);

                if (message != null)
                {
                    break;
                }
            }

            if (message == null)
            {
                /*
                 * Relax for 1 ms in order to make other threads more
                 * concurrent.
                 */
                this.relax(1);
            }
            else
            {
                switch (message.type())
                {
                case "CloseCollaborationRequest":
                    this.handleMessageCloseCollaborationRequest(message);
                    break;
                case "CloseCollaborationConfirm":
                    this.handleMessageCloseCollaborationConfirm(message);
                    break;
                default:
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
    public void setEmployeeName(String name)
    {
        this.employeeName = name;
    }

    /**
     * Get employee name.
     *
     * @return - employee name.
     */
    public String getEmployeeName()
    {
        return this.employeeName;
    }

    /**
     * Set manager.
     *
     * @param manager - manager.
     */
    public void setManager(Manager manager)
    {
        this.manager = manager;
    }

    /**
     * Main employee program.
     */
    public abstract void main();

    /**
     * Executable part for Thread.
     */
    @Override
    public void run()
    {
        this.main();
    }
}
