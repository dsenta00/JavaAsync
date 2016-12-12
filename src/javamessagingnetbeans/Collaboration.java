package javamessagingnetbeans;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    /**
     * Print collaboration trace.
     *
     * @param string - string to print.
     */
    private void trace(String string)
    {
        Trace.print("{" + this.first.name() + ", "
            + this.second.name() + "} -> " + string);
    }

    /**
     * Constructor.
     *
     * @param employee1 - fist employee.
     * @param employee2 - second employee.
     */
    public Collaboration(Employee employee1, Employee employee2)
    {
        this.messageQueue = new ConcurrentLinkedQueue<>();
        this.first = employee1;
        this.second = employee2;

        this.trace("Collaboration created");
    }

    /**
     * Send message to employee.
     *
     * @param sender - message sender.
     * @param message - the message.
     */
    public void send(Employee sender, Message message)
    {
        message.setOwner(sender == this.first ? this.second : this.first);

        this.trace(sender.name() + " sending " + message.type());

        /*
         * If another Thread (another Employee) tries to access messageQueue in
         * order to modify it, offer() will return false.
         *
         * In that case, offer() is forced to repeat until message is put in
         * queue.
         */
        while (!this.messageQueue.offer(message))
        {
        }
    }

    /**
     * Clean queue completely. Put on deprecated in order to avoid user to using
     * it.
     */
    @Deprecated
    public void cleanQueue()
    {
        this.trace("cleaning queue!");

        while (!this.messageQueue.isEmpty())
        {
            this.messageQueue.poll();
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
        Message message = this.messageQueue.peek();

        return (message != null && message.access(receiver))
            ? this.messageQueue.poll() : null;
    }
}
