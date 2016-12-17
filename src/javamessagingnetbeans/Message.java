package javamessagingnetbeans;

/**
 * Employees are exchanging information through Messages.
 */
public class Message
{

    /*
     * Message sender.
     */
    private final Employee sender;

    /*
     * Message owner. On message creation, message sender is message owner.
     * After message sending, message receiver becomes message owner.
     */
    private Employee owner;

    /*
     * Message type.
     */
    protected final String type;

    /*
     * The message content.
     */
    private final Object content;

    /**
     * Constructor.
     *
     * @param sender - sender.
     * @param content - message content.
     */
    public Message(Employee sender, Object content)
    {
        this.sender = sender;
        this.content = content;
        owner = sender;
        type = content.getClass().getSimpleName();
    }

    /**
     * Constructor for dummy message.
     *
     * @param sender - sender.
     */
    public Message(Employee sender)
    {
        this.sender = sender;
        owner = sender;
        content = null;
        type = "";
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
     * Get message content.
     *
     * @param <T> - message type.
     * @return message content.
     */
    @SuppressWarnings("unchecked")
    public <T> T content()
    {
        return (T) content;
    }
}
