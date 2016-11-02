package test;

import javamessagingnetbeans.Employee;
import javamessagingnetbeans.Manager;
import javamessagingnetbeans.Trace;

public class MainTest
{
    public static void main(String[] args)
    {
        Trace.traceOn();
        Manager manager = new Manager();
        Employee employee;
        
        employee = manager.createEmployee(EmployeeTest.class,
                "EmployeeTest");
        employee.start();
    }
}
