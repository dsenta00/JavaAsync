package javaasync;

/**
 * Employee competence.
 */
public abstract class EmployeeCompetence implements Runnable
{

    /*
     * This message.
     */
    private final String messageType;

    /*
     * The message.
     */
    private Message message;

    /**
     * Constructor.
     *
     * @param <T> - message type.
     * @param message message.
     */
    public <T> EmployeeCompetence(Class<T> message)
    {
        messageType = message.getSimpleName();
    }

    /**
     * Get message.
     *
     * @return message.
     */
    protected Message message()
    {
        return message;
    }

    /**
     * Me :)
     *
     * @return me.
     */
    protected Employee me()
    {
        return message.owner();
    }

    /**
     * Get Message type.
     *
     * @return Message type.
     */
    public String messageType()
    {
        return messageType;
    }

    /**
     * Run.
     */
    @Override
    public abstract void run();

    /**
     * Execute.
     *
     * @param message - the message.
     */
    public void execute(Message message)
    {
        this.message = message;
        new Thread(this).start();
    }
}
