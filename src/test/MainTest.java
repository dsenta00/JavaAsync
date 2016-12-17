package test;

import javaasync.Manager;

/**
 * Main test.
 */
public class MainTest
{

    /**
     * Main test.
     *
     * @param args - program arguments.
     */
    public static void main(String[] args)
    {
        Manager manager = new Manager();

        manager.traceOn();
        manager.secretaryOn();

        manager.createEmployee(EmployeeTest.class, "EmployeeTest").start();
    }
}
