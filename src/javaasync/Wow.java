package javaasync;

import javaasync.message.CollaborationMessage;

/**
 * Way of working. Protocol between Consultant and Employee.
 */
public abstract class Wow
{

    /**
     * See if conversation ended.
     *
     * @param word - word input.
     * @return true if conversation ended, otherwise false.
     */
    public abstract boolean conversationEnd(String word);

    /**
     * Decode value into message.
     *
     * @param value - value input.
     * @return CollaborationMessage
     */
    public abstract CollaborationMessage decode(String value);

    /**
     * Code message into string stream.
     *
     * @param message - the message.
     * @return string stream output if success, otherwise return an empty
     * string.
     */
    public abstract String code(CollaborationMessage message);
}
