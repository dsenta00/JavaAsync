package test;

import javaasync.Employee;
import javaasync.EmployeeCompetence;

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
        send(new MessageTest2(0), employee);
        send(new MessageTest3(), this, 1000);
    }

    @Override
    public void registryCompetences()
    {
        competence(new EmployeeCompetence(MessageTest2.class)
        {
            @Override
            public void run()
            {
                MessageTest2 info = message().content();
                info.print();

                me().send(new StupidMessage(), message().sender());

                if (info.getNumber() > 100000)
                {
                    me().manager().bancrupt();
                }
                else
                {
                    me().send(new MessageTest2(info.getNumber() + 1), message().sender());
                }
            }
        });

        competence(new EmployeeCompetence(MessageTest3.class)
        {
            @Override
            public void run()
            {
                MessageTest3 info = message().content();
                info.print();
                send(message().content(), me(), 1000);
            }
        });
    }
}
