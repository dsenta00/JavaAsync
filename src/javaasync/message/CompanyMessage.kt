package javaasync.message

import javaasync.AsyncService

/**
 * Company message.
 */
open class CompanyMessage(owner: AsyncService) : Message(owner) {

    /**
     * Check if message is used.
     *
     * @return always true.
     */
    override fun used(): Boolean {
        return true
    }
}
