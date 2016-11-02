package test;

import javamessagingnetbeans.Employee;
import javamessagingnetbeans.Message;

public class EmployeeTest2 extends Employee
{
    EmployeeTest3 employeeTest3;

    /**
     * Init employee.
     */
    private void init()
    {
        this.employeeTest3 = this.createEmployee(EmployeeTest3.class,
                "EmployeeTest3");
        this.employeeTest3.start();
        this.setupCollaboration(this.employeeTest3);
        this.sendMessage(new MessageTest2(1), this.employeeTest3);
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

        this.sendMessage(new MessageTest2(info.getNumber() + 2),
                message.sender());
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
                this.exception("Unkown signal received! => " + message.type());
                break;
            }
        }
    }
}
