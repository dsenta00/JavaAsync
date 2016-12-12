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
    private final String type;

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
        this.owner = sender;
        this.content = content;

        /*
         * 1) Remove "class " from string.
         *
         * Example:
         *
         *   class java.lang.lib.SomeMessage => java.lang.lib.SomeMessage
         *
         * 2) Remove package path from string.
         *
         * Example:
         *
         *   java.lang.lib.SomeMessage => SomeMessage
         */
        this.type = content.getClass().toString().replace("class ", "")
            .replaceAll("^.*?(\\w+)\\W*$", "$1");
    }

    /**
     * Set receiver as owner.
     *
     * @param employee - receiver to set as owner.
     */
    public void setOwner(Employee employee)
    {
        this.owner = employee;
    }

    /**
     * Get message type.
     *
     * @return string that represents message type.
     */
    public String type()
    {
        return this.type;
    }

    /**
     * Check if employee have access to this.
     *
     * @param employee - employee to check.
     * @return true if employee have access, otherwise false.
     */
    public boolean access(Employee employee)
    {
        return employee.name().equals(this.owner.name());
    }

    /**
     * Get sender.
     *
     * @return sender.
     */
    public Employee sender()
    {
        return this.sender;
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
        return (T) this.content;
    }
}
