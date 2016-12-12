package test;

import javamessagingnetbeans.Employee;
import javamessagingnetbeans.Manager;
import javamessagingnetbeans.Trace;

public class MainTest
{

    /**
     * Main test.
     *
     * @param args - program arguments.
     */
    public static void main(String[] args)
    {
        Trace.on();
        Manager manager = new Manager();
        Employee employee;

        employee = manager.createEmployee(EmployeeTest.class,
            "EmployeeTest");
        employee.start();
    }
}
