package test;

import javaasync.Employee;
import javaasync.Message;
import javaasync.escalation.UnkownMessageEscalation;

/**
 * Employee test 3.
 */
public class EmployeeTest3 extends Employee
{

    /**
     * Handle MessageTest3 message.
     *
     * @param message - The message.
     */
    private void handleMessageTest3(Message message)
    {
        MessageTest3 info = message.content();

        info.print();

        send(message.content(), this, 1000);
    }

    /**
     * Handle MessageTest2 message.
     *
     * @param message - the message.
     */
    private void handleMessageTest2(Message message)
    {
        MessageTest2 info = message.content();

        info.print();

        send(new StupidMessage(), message.sender());

        if (info.getNumber() > 100000)
        {
            manager().bancrupt();
        }
        else
        {
            send(new MessageTest2(info.getNumber() + 1), message.sender());
        }
    }

    @Override
    public void init()
    {
        Employee employee = manager().getEmployee("EmployeeTest");
        setupCollaboration(employee);
        send(new MessageTest2(0), employee);
        send(new MessageTest3(), this, 1000);
    }

    @Override
    public void messageEvent(Message message)
    {
        switch (message.type())
        {
            case "MessageTest2":
                handleMessageTest2(message);
                break;
            case "MessageTest3":
                handleMessageTest3(message);
                break;
            default:
                escalation(new UnkownMessageEscalation(message));
                break;
        }
    }
}
