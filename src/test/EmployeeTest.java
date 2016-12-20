package test;

import javaasync.Employee;
import javaasync.Competence;

/**
 * Employee test.
 */
public class EmployeeTest extends Employee
{

    @Override
    public void init()
    {
        EmployeeTest2 employeeTest2 = newEmployee(EmployeeTest2.class, "EmployeeTest2");
        employeeTest2.start();
        setupCollaboration(employeeTest2);
        send(new IntegerMessage(2), employeeTest2);
    }

    @Override
    public void registryCompetences()
    {
        /**
         * Add MessageTest competence.
         */
        competence(new Competence(IntegerMessage.class)
        {
            @Override
            public void run()
            {
                IntegerMessage info = message().content();
                info.print();
                me().send(new IntegerMessage(info.getNumber() + 1), message().sender());
            }
        });

        /**
         * Add MessageTest2 competence.
         */
        competence(new Competence(StringMessage.class)
        {
            @Override
            public void run()
            {
                StringMessage info = message().content();
                info.print();
                me().send(new StringMessage(info.getNumber() + 1), message().sender());
            }
        });

        /**
         * Add StupidMessage competence.
         */
        competence(new Competence(DummyMessage.class)
        {
            @Override
            public void run()
            {
                DummyMessage info = message().content();
                info.print();
            }
        });
    }
}
