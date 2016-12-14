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
    Map<String, Employee> employeeMap;

    /*
     * Every handsome manager needs to have a secretary.
     */
    Secretary secretary;

    /**
     * Constructor.
     */
    public Manager()
    {
        this.secretary = new Secretary("log_"
            + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".log");

        this.employeeMap = new HashMap<>();
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
        return this.secretary;
    }

    /**
     * Get employee.
     *
     * @param name - employees name.
     * @return Employee if exist, otherwise return null.
     */
    public Employee getEmployee(String name)
    {
        return this.employeeMap.get(name);
    }

    /**
     * Get number of employees.
     *
     * @return number of employees.
     */
    public int numOfEmployees()
    {
        return this.employeeMap.size();
    }

    /**
     * Remove employee.
     *
     * @param employee - employee to remove
     */
    public void removeEmployee(Employee employee)
    {
        this.employeeMap.remove(employee.name());
    }

    /**
     * Fire employee.
     *
     * @param name - employee name.
     * @return true if employee quit job successfully, otherwise return false.
     */
    public boolean fireEmployee(String name)
    {
        Employee employee = this.getEmployee(name);

        if (employee == null)
        {
            return false;
        }

        this.removeEmployee(employee);

        if (this.numOfEmployees() == 0)
        {
            this.closeCompany();
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
            this.employeeMap.keySet());

        for (String employeeName : employeeList)
        {
            this.fireEmployee(employeeName);
            this.wait(this.EMPLOYEE_NOTICE_PERIOD);
        }

        this.closeCompany();
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

            this.employeeMap.put(name, employee);
        }
        catch (InstantiationException | IllegalAccessException e)
        {
        }

        return employee;
    }
}
