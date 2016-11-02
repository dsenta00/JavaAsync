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
     * Message owner. On message creation, sender is message owner. After
     * message sending, receiver becomes message owner.
     */
    private Employee owner;

    /*
     * Message type.
     */
    private final String type;

    /*
     * The real message.
     */
    private final Object message;

    /**
     * Constructor.
     *
     * @param <T> - message type.
     * @param sender - sender.
     * @param message - message to send.
     */
    public <T> Message(Employee sender, T message) {
        this.sender = sender;
        this.owner = sender;
        this.message = message;

        /*
         * 1) Remove "class " string.
         *
         * class SomeMessage => SomeMessage
         *
         * 2) Remove library path
         *
         * java.lang.library.SomeMessage => SomeMessage
         */
        this.type = message.getClass().toString().replace("class ", "")
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
    public boolean checkAccess(Employee employee)
    {
        return employee.getEmployeeName()
                .equals(this.owner.getEmployeeName());
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
    public <T> T getMessage()
    {
        return (T) this.message;
    }
}
