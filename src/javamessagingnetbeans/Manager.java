package javamessagingnetbeans;

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

    /**
     * Constructor.
     */
    public Manager()
    {
        this.employeeMap = new HashMap<>();
    }

    /**
     * Manager is always waiting for some employee.
     *
     * @param milliseconds - milliseconds to wait.
     */
    private void wait(int milliseconds)
    {
        try
        {
            Thread.currentThread().sleep(milliseconds);
        }
        catch (InterruptedException ex)
        {
        }
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
        Trace.print("Closed company");
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
