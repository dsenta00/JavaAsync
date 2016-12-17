package test;

import javaasync.Employee;
import javaasync.Message;
import javaasync.escalation.UnkownMessageEscalation;

/**
 * Employee test.
 */
public class EmployeeTest extends Employee
{

    @Override
    public void init()
    {
        EmployeeTest2 employeeTest2 = createEmployee(EmployeeTest2.class,
            "EmployeeTest2");
        employeeTest2.start();
        setupCollaboration(employeeTest2);
        send(new MessageTest(2), employeeTest2);
    }

    @Override
    public void messageEvent(Message message)
    {
        switch (message.type())
        {
            case "MessageTest":
                handleMessageTest(message);
                break;
            case "MessageTest2":
                handleMessageTest2(message);
                break;
            case "StupidMessage":
                StupidMessage stupidMssg = message.content();
                stupidMssg.print();
                break;
            default:
                escalation(new UnkownMessageEscalation(message));
                break;
        }
    }

    /**
     * Handle MessageTest message.
     *
     * @param message - The message.
     */
    private void handleMessageTest(Message message)
    {
        MessageTest info = message.content();

        info.print();

        send(new MessageTest(info.getNumber() + 1), message.sender());
    }

    /**
     * Handle MessageTest2 message.
     *
     * @param message - The message.
     */
    private void handleMessageTest2(Message message)
    {
        MessageTest2 info = message.content();

        info.print();

        send(new MessageTest2(info.getNumber() + 1), message.sender());
    }
}
