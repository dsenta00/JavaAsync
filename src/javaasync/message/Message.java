package javaasync.message;

import javaasync.Employee;

/**
 * Employees are exchanging information through Messages.
 */
public class Message extends MessageBase
{

    /*
     * The message content.
     */
    private final Object content;

    /*
     * Check if message is used.
     */
    private boolean used;

    /**
     * Constructor.
     *
     * @param sender - sender.
     * @param content - message content.
     */
    public Message(Employee sender, Object content)
    {
        super(sender);
        this.content = content;
        used = false;
        type = content.getClass().getSimpleName();
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
        used = true;
        return (T) content;
    }

    /**
     * Check if message is used.
     *
     * @return true if used, otherwise false.
     */
    @Override
    public boolean used()
    {
        return used;
    }
}
