package test;

import javaasync.Competence;
import javaasync.Employee;

/**
 * Employee test 3.
 */
public class EmployeeTest3 extends Employee
{

    @Override
    public void init()
    {
        Employee employee = manager().getEmployee("EmployeeTest");
        setupCollaboration(employee);
        send(new StringMessage(0), employee);
        send(new TmoMessage(), this, 1000);
    }

    @Override
    public void registryCompetences()
    {
        /**
         * MessageTest2 competence.
         */
        competence(new Competence(StringMessage.class)
        {
            @Override
            public void run()
            {
                StringMessage info = message().content();
                info.print();

                me().send(new DummyMessage(), message().sender());

                if (info.getNumber() > 100000)
                {
                    me().manager().bancrupt();
                }
                else
                {
                    me().send(new StringMessage(info.getNumber() + 1), message().sender());
                }
            }
        });

        /**
         * MessageTest3 competence.
         */
        competence(new Competence(TmoMessage.class)
        {
            @Override
            public void run()
            {
                TmoMessage info = message().content();
                info.print();
                send(message().content(), me(), 1000);
            }
        });
    }
}
