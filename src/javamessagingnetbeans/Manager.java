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
     * Employee map.
     */
    Map<String, Employee> employeeMap;

    /**
     * Constructor.
     */
    public Manager() {
        this.employeeMap = new HashMap<>();
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
     * Remove employee.
     *
     * @param employee - employee to remove
     */
    public void removeEmployee(Employee employee)
    {
        this.employeeMap.remove(employee.getEmployeeName());
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

        employee.quitJob();

        return true;
    }

    /**
     * Destroy company.
     */
    public void bancrupt()
    {
        List<String> employeeList = new LinkedList<>(
                this.employeeMap.keySet());

        for (String employeeName : employeeList)
        {
            this.fireEmployee(employeeName);

            /*
             * Wait for employee to be removed from system.
             */
            while (this.employeeMap.containsKey(employeeName))
            {
            }
        }
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

        if (this.employeeMap.containsKey(name))
        {
            try
            {
                throw new Exception(
                        "Employee with name " + name + "already exist!");
            }
            catch (Exception e)
            {
            }
            return null;
        }

        try
        {
            employee = clazz.newInstance();
            employee.setManager(this);
            employee.setEmployeeName(name);

            this.employeeMap.put(name, employee);
        }
        catch (InstantiationException | IllegalAccessException e)
        {
        }

        return employee;
    }
}
