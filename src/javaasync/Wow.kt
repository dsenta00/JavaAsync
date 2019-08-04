package javaasync

import javaasync.message.CollaborationMessage

/**
 * Way of working. Protocol between Consultant and Employee.
 */
abstract class Wow {

    /**
     * See if conversation ended.
     *
     * @param word - word input.
     * @return true if conversation ended, otherwise false.
     */
    abstract fun conversationEnd(word: String): Boolean

    /**
     * Decode value into message.
     *
     * @param value - value input.
     * @return CollaborationMessage
     */
    abstract fun decode(value: String): CollaborationMessage

    /**
     * Code message into string stream.
     *
     * @param message - the message.
     * @return string stream output if success, otherwise return an empty
     * string.
     */
    abstract fun code(message: CollaborationMessage): String
}
