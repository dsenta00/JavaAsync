package test;

import javamessagingnetbeans.Employee;
import javamessagingnetbeans.Message;

public class EmployeeTest2 extends Employee
{

    EmployeeTest3 employeeTest3;

    @Override
    public void init()
    {
        employeeTest3 = createEmployee(EmployeeTest3.class,
            "EmployeeTest3");
        employeeTest3.start();
        setupCollaboration(employeeTest3);
        send(new MessageTest2(1), employeeTest3);
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
                stupidMssg.read();
                break;
            default:
                exception("Unkown signal received! => " + message.type());
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

        info.read();

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

        info.read();

        send(new MessageTest2(info.getNumber() + 1), message.sender());
    }
}
