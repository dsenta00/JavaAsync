package test;

import javamessagingnetbeans.Employee;
import javamessagingnetbeans.Message;

public class EmployeeTest extends Employee
{

    @Override
    public void init()
    {
        EmployeeTest2 employeeTest2 = this.createEmployee(EmployeeTest2.class,
            "EmployeeTest2");
        employeeTest2.start();
        this.setupCollaboration(employeeTest2);
        this.send(new MessageTest(2), employeeTest2);
    }

    @Override
    public void messageEvent(Message message)
    {
        switch (message.type())
        {
            case "MessageTest":
                this.handleMessageTest(message);
                break;
            case "MessageTest2":
                this.handleMessageTest2(message);
                break;
            case "KataMessage":
                KataMessage kata = message.content();
                kata.read();
                break;
            default:
                this.exception("Unkown signal received!");
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

        this.send(new MessageTest(info.getNumber() + 2), message.sender());
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

        if (info.getNumber() < 50)
        {
            this.send(new MessageTest2(info.getNumber() + 2),
                message.sender());
        }
        else
        {
            this.trace("Kill yourself mighty " + message.sender());
            this.send(new MessageTest3(), message.sender());
        }
    }
}
