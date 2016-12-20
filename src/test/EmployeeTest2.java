package test;

import javaasync.Competence;
import javaasync.Employee;

/**
 * Employee test 2.
 */
public class EmployeeTest2 extends Employee
{

    EmployeeTest3 employeeTest3;

    @Override
    public void init()
    {
        employeeTest3 = newEmployee(EmployeeTest3.class, "EmployeeTest3");
        employeeTest3.start();
        setupCollaboration(employeeTest3);
        send(new StringMessage(1), employeeTest3);
    }

    @Override
    public void registryCompetences()
    {
        /**
         * MessageTest competence.
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
         * MessageTest2 competence.
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
         * StupidMessage competence.
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
