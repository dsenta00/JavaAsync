package test;

import javamessagingnetbeans.Employee;
import javamessagingnetbeans.Message;

public class EmployeeTest3 extends Employee
{

    /**
     * Handle MessageTest3 message.
     *
     * @param message - The message.
     */
    private void handleMessageTest3(Message message)
    {
        this.manager().fireEmployee(this.name());
    }

    /**
     * Handle MessageTest2 message.
     *
     * @param message - the message.
     */
    private void handleMessageTest2(Message message)
    {
        MessageTest2 info = message.content();

        info.read();

        if (info.getNumber() > 5000)
        {
            this.send(new StupidMessage(), message.sender());
        }

        if (info.getNumber() > 100000)
        {
            this.manager().bancrupt();
        }
        else
        {
            this.send(new MessageTest2(info.getNumber() + 1), message.sender());
        }
    }

    @Override
    public void init()
    {
        Employee employee = this.manager().getEmployee("EmployeeTest");
        this.setupCollaboration(employee);
        this.send(new MessageTest(0), employee);
    }

    @Override
    public void messageEvent(Message message)
    {
        switch (message.type())
        {
            case "MessageTest2":
                this.handleMessageTest2(message);
                break;
            case "MessageTest3":
                this.handleMessageTest3(message);
                break;
            default:
                this.exception("Unkown signal received!");
                break;
        }
    }
}
