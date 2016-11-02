package test;

import javamessagingnetbeans.Employee;
import javamessagingnetbeans.Message;

public class EmployeeTest extends Employee
{
    EmployeeTest2 employeeTest2;

    /**
     * Init employee.
     */
    private void init()
    {
        this.employeeTest2 = this.createEmployee(EmployeeTest2.class,
                "EmployeeTest2");
        this.employeeTest2.start();
        this.setupCollaboration(this.employeeTest2);
        this.sendMessage(new MessageTest(2), this.employeeTest2);
    }

    /**
     * Handle MessageTest message.
     *
     * @param message - The message.
     */
    private void handleMessageTest(Message message)
    {
        MessageTest info = message.getMessage();

        info.read();

        this.sendMessage(new MessageTest(info.getNumber() + 2),
                message.sender());
    }

    /**
     * Handle MessageTest2 message.
     *
     * @param message - The message.
     */
    private void handleMessageTest2(Message message)
    {
        MessageTest2 info = message.getMessage();

        info.read();

        if (info.getNumber() < 150)
        {
            this.sendMessage(new MessageTest2(info.getNumber() + 2),
                    message.sender());
        }
        else
        {
            this.sendMessage(new MessageTest3(), message.sender());
        }
    }

    @Override
    public void main()
    {
        this.init();

        for (;;)
        {
            Message message = this.receiveMessage();

            switch (message.type())
            {
            case "MessageTest":
                this.handleMessageTest(message);
                break;
            case "MessageTest2":
                this.handleMessageTest2(message);
                break;
            default:
                this.exception("Unkown signal received!");
                break;
            }
        }
    }
}
