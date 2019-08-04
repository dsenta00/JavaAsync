package javaasync.message

import javaasync.AsyncService

/**
 * Employees are exchanging information through Messages.
 */
class CollaborationMessage(sender: AsyncService, private val content: Any) : Message(sender) {

    /*
     * Check if message is used.
     */
    private var used: Boolean = false

    init {
        this.used = false
        type = content.javaClass.simpleName
    }

    /**
     * Get message content.
     *
     * @param <T> - message type.
     * @return message content.
    </T> */
    fun <T> content(): T {
        used = true
        return content as T
    }

    /**
     * Check if message is used.
     *
     * @return true if used, otherwise false.
     */
    override fun used(): Boolean {
        return used
    }
}
