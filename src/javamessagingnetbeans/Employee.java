package javamessagingnetbeans;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Employee class represents a independent worker. Each employee is ran through
 * its own thread. Employees can collaborate in order to exchange messages.
 */
public abstract class Employee extends Thread
{

    /*
     * Quick relax -> 1 millisecond.
     */
    private final int QUICK_RELAX = 1;

    /*
     * Big relax -> 10 millisecond.
     */
    private final int BIG_RELAX = 10;

    /*
     * Treshold for noOfNotMessageReceived counter. If noOfNotMessageReceived
     * reach this value, Employee will take BIG_RELAX instead of QUICK_RELAX.
     */
    private final int NO_OF_NOT_MESSAGE_RECEIVED_TRESHOLD = 5;

    /*
     * Limit for not message received. If noOfNotMessage received
     * reach this limit, this thread will terminate it self.
     *
     * If Employee is idle for 10 minutes (600 seconds/600,000 ms)
     * Employee is considered to use to many resources and Employee should
     * terminate itself. Approximatelly every 10 ms counter is increased by one.
     */
    private final int NO_OF_NOT_MESSAGE_RECEIVED_EXIT = 60000;

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
     * Number of time not message received.
     */
    private int noOfNotMessageReceived;

    /*
     * Flag to check if employee is leaving job.
     */
    private boolean leavingJob;

    /**
     * Constructor.
     */
    public Employee()
    {
        this.manager = null;
        this.employeeName = null;
        this.collaborationMap = new ConcurrentHashMap<>();
        this.noOfNotMessageReceived = 0;
        this.leavingJob = false;
    }

    /**
     * Handle not received message.
     */
    private void handleNotReceivedMessage()
    {
        this.noOfNotMessageReceived++;

        /*
         * This employee isn't useful. Fire him!
         */
        if (this.noOfNotMessageReceived > this.NO_OF_NOT_MESSAGE_RECEIVED_EXIT)
        {
            manager().fireEmployee(this.employeeName);
        }

        /*
         * Relax for some time in order to make other threads more
         * concurrent.
         */
        this.relax((this.noOfNotMessageReceived
            < this.NO_OF_NOT_MESSAGE_RECEIVED_TRESHOLD)
                ? this.QUICK_RELAX : this.BIG_RELAX);
    }

    /**
     * Leave company.
     */
    private void leaveCompany()
    {
        this.trace("just left company");
        Thread.currentThread().stop();
    }

    /**
     * Handle message CloseCollaborationRequest.
     *
     * @param message - CloseCollaborationRequest message
     */
    private void handleMessageCloseCollaborationRequest(Message message)
    {
        Collaboration collaboration = this.getCollaboration(message.sender());

        if (collaboration == null)
        {
            this.exception("Collaboration with employee "
                + message.sender().name() + " doesn't exist.");
            return;
        }

        this.trace("closing collaboration with " + message.sender().name());

        collaboration.cleanQueue();
        this.send(new CloseCollaborationConfirm(), message.sender());
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

        this.trace("closed collaboration with " + message.sender().name());

        if (this.collaborationMap.isEmpty())
        {
            /*
             * All collaborations are closed.
             * Leave company.
             */
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
            this.exception("collaboration already exist!");
            return;
        }

        this.trace("offered collaboration from " + employee.name());

        this.collaborationMap.put(employee, collaboration);
    }

    /**
     * Get employees manager.
     *
     * @return The Manager.
     */
    protected Manager manager()
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
        return this.getEmployee(name) == null
            ? this.manager.createEmployee(clazz, name)
            : null;
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

        this.trace("setup collaboration with " + employee.name());

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
    protected <T> void send(T message, Employee toEmployee)
    {
        Collaboration collaboration = this.getCollaboration(toEmployee);

        if (collaboration == null)
        {
            this.exception("Collaboration doesn't exist!");
            return;
        }

        collaboration.send(this, new Message(this, message));
    }

    /**
     * Send all collaboration request in order to quit job.
     */
    public void quitJob()
    {
        this.trace("leaving company");

        this.leavingJob = true;

        if (this.collaborationMap.isEmpty())
        {
            this.leaveCompany();
            return;
        }

        for (Entry<Employee, Collaboration> entry : this.collaborationMap
            .entrySet())
        {
            entry.getValue().cleanQueue();
            this.send(new CloseCollaborationRequest(), entry.getKey());
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
        Trace.print(this.name() + " -> " + string);
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
                this.handleNotReceivedMessage();
                continue;
            }

            this.noOfNotMessageReceived = 0;

            switch (message.type())
            {
                case "CloseCollaborationRequest":
                    this.handleMessageCloseCollaborationRequest(message);
                    break;
                case "CloseCollaborationConfirm":
                    this.handleMessageCloseCollaborationConfirm(message);
                    break;
                default:
                    if (!this.leavingJob)
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
        this.employeeName = name;
    }

    /**
     * Get employee name.
     *
     * @return - employee name.
     */
    public String name()
    {
        return this.employeeName;
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
     * Initialize.
     */
    public abstract void init();

    /**
     * Message event handler.
     *
     * @param message - received message.
     */
    public abstract void messageEvent(Message message);

    /**
     * Executable part for Thread.
     */
    @Override
    public void run()
    {
        this.init();

        for (;;)
        {
            messageEvent(this.receive());
        }
    }
}
