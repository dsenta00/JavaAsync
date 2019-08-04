package javaasync

import javaasync.message.CloseCollaborationConfirm
import javaasync.message.CloseCollaborationRequest
import javaasync.message.CompanyMessage
import javaasync.message.Message

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Collaboration class represents collaboration between two employees. This
 * class also contains information about two employees and concurrent queue with
 * messages.
 */
class Collaboration(private val first: AsyncService, private val second: AsyncService) {

    /*
     * Message queue.
     */
    private val messageQueue: Queue<Message>

    /*
     * Flag to indicate if collaboration is closing.
     */
    private var closing: Boolean = false

    /**
     * Print collaboration log.
     *
     * @param string - string to print.
     */
    private fun log(string: String) {
        /*first.manager.secretary.log("{" + first.name() + ", "
                + second.name() + "} -> " + string
        )*/
    }

    init {
        messageQueue = ConcurrentLinkedQueue()
        closing = false

        log("Collaboration created")
    }

    /**
     * See if collaboration is closing.
     *
     * @return true if closing, otherwise false.
     */
    fun closing(): Boolean {
        return closing
    }

    /**
     * Return first collaborator.
     *
     * @return first collaborator.
     */
    fun first(): AsyncService {
        return first
    }

    /**
     * Return second collaborator.
     *
     * @return second collaborator.
     */
    fun second(): AsyncService {
        return second
    }

    /**
     * Send message to employee.
     *
     * @param sender  - message sender.
     * @param message - the message.
     */
    fun send(sender: AsyncService, message: Message) {
        message.setOwner(if (sender === first) second else first)

        log(
            sender.name() + " sending "
                    + if (message is CompanyMessage)
                message.javaClass.simpleName
            else
                message.type()
        )

        if (message is CloseCollaborationRequest) {
            closing = true
        }

        /*
         * If another Thread (another Employee) tries to access messageQueue in
         * order to modify it, offer() will return false.
         *
         * In that case, offer() is forced to repeat until message is put in
         * queue.
         */
        while (!messageQueue.offer(message)) {
        }
    }

    /**
     * Receive message.
     *
     * @param receiver - message receiver.
     * @return Message if messageQueue isn't empty and if receiver has access to
     * message, otherwise return null.
     */
    fun receiveMessage(receiver: AsyncService): Message? {
        val message = messageQueue.peek()

        return if (message != null
            && (!closing || message is CloseCollaborationConfirm)
            && message.access(receiver)
        )
            messageQueue.poll()
        else
            null
    }
}
