package javamessagingnetbeans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * class Manager creates and manages employees.
 */
public class Manager
{

    /*
     * Employees notice period.
     */
    private final int EMPLOYEE_NOTICE_PERIOD = 20;

    /*
     * Employee map.
     */
    private final Map<String, Employee> employeeMap;

    /*
     * Every handsome manager needs to have a secretary.
     */
    private final Secretary secretary;

    /**
     * Constructor.
     */
    public Manager()
    {
        secretary = new Secretary(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
            .format(new Date()) + ".log");

        employeeMap = new HashMap<>();
    }

    /**
     * Put traces on.
     */
    public void traceOn()
    {
        Trace.on();
    }

    /**
     * Put traces off.
     */
    public void traceOff()
    {
        Trace.off();
    }

    /**
     * Turn secretary on.
     */
    public void secretaryOn()
    {
        secretary.on();
    }

    /**
     * Turn secretary off.
     */
    public void secretaryOff()
    {
        secretary.off();
    }

    /**
     * Manager is always waiting for some employee.
     *
     * @param nanoseconds - nanoseconds to wait.
     */
    private void wait(int nanoseconds)
    {
        try
        {
            Thread.currentThread().sleep(0, nanoseconds);
        }
        catch (InterruptedException ex)
        {
        }
    }

    /**
     * Get secretary.
     *
     * @return The secretary.
     */
    public Secretary getSecretary()
    {
        return secretary;
    }

    /**
     * Get employee.
     *
     * @param name - employees name.
     * @return Employee if exist, otherwise return null.
     */
    public Employee getEmployee(String name)
    {
        return employeeMap.get(name);
    }

    /**
     * Get number of employees.
     *
     * @return number of employees.
     */
    public int numOfEmployees()
    {
        return employeeMap.size();
    }

    /**
     * Remove employee.
     *
     * @param employee - employee to remove
     */
    public void removeEmployee(Employee employee)
    {
        employeeMap.remove(employee.name());
    }

    /**
     * Fire employee.
     *
     * @param name - employee name.
     * @return true if employee quit job successfully, otherwise return false.
     */
    public boolean fireEmployee(String name)
    {
        Employee employee = getEmployee(name);

        if (employee == null)
        {
            return false;
        }

        removeEmployee(employee);

        if (numOfEmployees() == 0)
        {
            closeCompany();
        }
        else
        {
            employee.quitJob();
        }

        return true;
    }

    /**
     * Close company.
     */
    public void closeCompany()
    {
        secretary.log("Closed company!");
        secretary.giveLayOffPay();
        Runtime.getRuntime().halt(0);
    }

    /**
     * Fire all employees and terminate application.
     */
    public void bancrupt()
    {
        List<String> employeeList = new LinkedList<>(
            employeeMap.keySet());

        for (String employeeName : employeeList)
        {
            fireEmployee(employeeName);
        }

        closeCompany();
    }

    /**
     * Create new employee instance.
     *
     * @param <T> - Employee type.
     * @param clazz - employee type.
     * @param name - employee name.
     * @return Employee if success, otherwise null.
     */
    public <T extends Employee> T createEmployee(Class<T> clazz, String name)
    {
        T employee = null;

        try
        {
            employee = clazz.newInstance();
            employee.manager(this);
            employee.name(name);

            employeeMap.put(name, employee);
        }
        catch (InstantiationException | IllegalAccessException e)
        {
        }

        return employee;
    }
}
