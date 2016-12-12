package test;

import javamessagingnetbeans.Employee;
import javamessagingnetbeans.Message;

public class EmployeeTest2 extends Employee
{

    EmployeeTest3 employeeTest3;

    @Override
    public void init()
    {
        this.employeeTest3 = this.createEmployee(EmployeeTest3.class,
            "EmployeeTest3");
        this.employeeTest3.start();
        this.setupCollaboration(this.employeeTest3);
        this.send(new MessageTest2(1), this.employeeTest3);
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
                this.exception("Unkown signal received! => " + message.type());
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

        this.send(new MessageTest2(info.getNumber() + 2), message.sender());
    }
}
