package javaasync.message;

import javaasync.Employee;

/**
 * Company message.
 */
public class CompanyMessage extends Message
{

    /**
     * The constructor.
     *
     * @param owner - message owner.
     */
    public CompanyMessage(Employee owner)
    {
        super(owner);
    }

    /**
     * Check if message is used.
     *
     * @return always true.
     */
    @Override
    public boolean used()
    {
        return true;
    }
}
