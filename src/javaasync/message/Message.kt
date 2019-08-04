package javaasync.message

import javaasync.AsyncService

/**
 * Message base.
 */
abstract class Message(protected val sender: AsyncService) {
    /*
     * Message owner. On message creation, message sender is message owner.
     * After message sending, message receiver becomes message owner.
     */
    private var owner: AsyncService

    /*
     * Message type.
     */
    protected var type: String? = null

    init {
        this.owner = sender
        type = null
    }

    /**
     * Set receiver as owner.
     *
     * @param asyncService - receiver to set as owner.
     */
    fun setOwner(asyncService: AsyncService) {
        owner = asyncService
    }

    /**
     * Get message type.
     *
     * @return string that represents message type.
     */
    fun type(): String? {
        return type
    }

    /**
     * Get owner name.
     *
     * @return owner name.
     */
    fun owner(): AsyncService? {
        return owner
    }

    /**
     * Check if employee have access to this.
     *
     * @param asyncService - employee to check.
     * @return true if employee have access, otherwise false.
     */
    fun access(asyncService: AsyncService): Boolean {
        return asyncService.name() == owner.name()
    }

    /**
     * Get sender.
     *
     * @return sender.
     */
    fun sender(): AsyncService {
        return sender
    }

    /**
     * Check if message is used.
     *
     * @return true if used, otherwise false.
     */
    abstract fun used(): Boolean
}
