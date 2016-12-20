package javaasync.message;

import javaasync.Employee;

/**
 * Message base.
 */
public abstract class Message
{

    /*
     * Message sender.
     */
    protected final Employee sender;

    /*
     * Message owner. On message creation, message sender is message owner.
     * After message sending, message receiver becomes message owner.
     */
    private Employee owner;

    /*
     * Message type.
     */
    protected String type;

    /**
     * The constructor.
     *
     * @param owner - Message owner.
     */
    public Message(Employee owner)
    {
        sender = owner;
        this.owner = owner;
        type = null;
    }

    /**
     * Set receiver as owner.
     *
     * @param employee - receiver to set as owner.
     */
    public void setOwner(Employee employee)
    {
        owner = employee;
    }

    /**
     * Get message type.
     *
     * @return string that represents message type.
     */
    public String type()
    {
        return type;
    }

    /**
     * Get owner name.
     *
     * @return owner name.
     */
    public Employee owner()
    {
        return owner;
    }

    /**
     * Check if employee have access to this.
     *
     * @param employee - employee to check.
     * @return true if employee have access, otherwise false.
     */
    public boolean access(Employee employee)
    {
        return employee.name().equals(owner.name());
    }

    /**
     * Get sender.
     *
     * @return sender.
     */
    public Employee sender()
    {
        return sender;
    }

    /**
     * Check if message is used.
     *
     * @return true if used, otherwise false.
     */
    public abstract boolean used();
}
