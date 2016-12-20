package test;

import javaasync.Employee;
import javaasync.EmployeeCompetence;

/**
 * Employee test 2.
 */
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
    public void registryCompetences()
    {
        competence(new EmployeeCompetence(MessageTest.class)
        {
            @Override
            public void run()
            {
                MessageTest info = message().content();
                info.print();
                me().send(new MessageTest(info.getNumber() + 1), message().sender());
            }
        });

        competence(new EmployeeCompetence(MessageTest2.class)
        {
            @Override
            public void run()
            {
                MessageTest2 info = message().content();
                info.print();
                me().send(new MessageTest2(info.getNumber() + 1), message().sender());
            }
        });

        competence(new EmployeeCompetence(StupidMessage.class)
        {
            @Override
            public void run()
            {
                StupidMessage info = message().content();
                info.print();
            }
        });
    }
}
