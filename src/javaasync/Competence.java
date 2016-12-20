package javaasync;

import javaasync.message.CollaborationMessage;
import javaasync.message.Message;

/**
 * Employees competence.
 */
public abstract class Competence implements Runnable
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
    public <T> Competence(Class<T> message)
    {
        messageType = message.getSimpleName();
    }

    /**
     * Get message.
     *
     * @return message.
     */
    protected CollaborationMessage message()
    {
        return (CollaborationMessage) message;
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
     * Get CollaborationMessage type.
     *
     * @return CollaborationMessage type.
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
