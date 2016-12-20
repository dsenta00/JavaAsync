package javaasync;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javaasync.message.CloseCollaborationConfirm;
import javaasync.message.CloseCollaborationRequest;
import javaasync.message.CompanyMessage;
import javaasync.message.Message;

/**
 * Collaboration class represents collaboration between two employees. This
 * class also contains information about two employees and concurrent queue with
 * messages.
 */
public class Collaboration
{

    /*
     * First employee.
     */
    private final Employee first;

    /*
     * Second employee.
     */
    private final Employee second;

    /*
     * Message queue.
     */
    private final Queue<Message> messageQueue;

    /*
     * Flag to indicate if collaboration is closing.
     */
    private boolean closing;

    /**
     * Print collaboration log.
     *
     * @param string - string to print.
     */
    private void log(String string)
    {
        first.manager().getSecretary().log("{" + first.name() + ", "
            + second.name() + "} -> " + string);
    }

    /**
     * Constructor.
     *
     * @param employee1 - fist employee.
     * @param employee2 - second employee.
     */
    public Collaboration(Employee employee1, Employee employee2)
    {
        messageQueue = new ConcurrentLinkedQueue<>();
        first = employee1;
        second = employee2;
        closing = false;

        log("Collaboration created");
    }

    /**
     * See if collaboration is closing.
     *
     * @return true if closing, otherwise false.
     */
    public boolean closing()
    {
        return closing;
    }

    /**
     * Return first collaborator.
     *
     * @return first collaborator.
     */
    public Employee first()
    {
        return first;
    }

    /**
     * Return second collaborator.
     *
     * @return second collaborator.
     */
    public Employee second()
    {
        return second;
    }

    /**
     * Send message to employee.
     *
     * @param sender - message sender.
     * @param message - the message.
     */
    public void send(Employee sender, Message message)
    {
        message.setOwner(sender == first ? second : first);

        log(sender.name() + " sending "
            + (((message instanceof CompanyMessage))
                ? message.getClass().getSimpleName()
                : message.type()));

        if (message instanceof CloseCollaborationRequest)
        {
            closing = true;
        }

        /*
         * If another Thread (another Employee) tries to access messageQueue in
         * order to modify it, offer() will return false.
         *
         * In that case, offer() is forced to repeat until message is put in
         * queue.
         */
        while (!messageQueue.offer(message))
        {
        }
    }

    /**
     * Receive message.
     *
     * @param receiver - message receiver.
     * @return Message if messageQueue isn't empty and if receiver has access to
     * message, otherwise return null.
     */
    public Message receiveMessage(Employee receiver)
    {
        Message message = messageQueue.peek();

        return (message != null
            && (!closing || message instanceof CloseCollaborationConfirm)
            && message.access(receiver))
            ? messageQueue.poll() : null;
    }
}
