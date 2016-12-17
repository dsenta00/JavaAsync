package javamessagingnetbeans;

/**
 * Way of working. Protocol between Consultant and Employee.
 */
abstract class Wow
{

    public abstract boolean conversationEnd(String word);

    public abstract Message decode(String value);

    public abstract String code(Message message);
}
