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
        this.getManager().bancrupt();
    }

    /**
     * Handle MessageTest2 message.
     *
     * @param message - the message.
     */
    private void handleMessageTest2(Message message)
    {
        MessageTest2 info = message.getMessage();

        info.read();

        if (info.getNumber() > 100)
        {
            this.getManager().fireEmployee(message.sender().getEmployeeName());
        }
        else
        {
            this.sendMessage(new MessageTest2(info.getNumber() + 2),
                    message.sender());
        }
    }

    @Override
    public void main()
    {
        for (;;)
        {
            Message message = this.receiveMessage();

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
}
