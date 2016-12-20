package test;

import javaasync.Employee;
import javaasync.EmployeeCompetence;

/**
 * Employee test.
 */
public class EmployeeTest extends Employee
{

    @Override
    public void init()
    {
        EmployeeTest2 employeeTest2 = createEmployee(EmployeeTest2.class,
            "EmployeeTest2");
        employeeTest2.start();
        setupCollaboration(employeeTest2);
        send(new MessageTest(2), employeeTest2);
    }

    @Override
    public void registryCompetences()
    {
        /**
         * Add MessageTest competence.
         */
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

        /**
         * Add MessageTest2 competence.
         */
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

        /**
         * Add StupidMessage competence.
         */
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
