package test;

/**
 * Message test 2.
 */
public class StringMessage
{

    private final String number;

    /**
     * The constructor.
     *
     * @param number - number.
     */
    public StringMessage(int number)
    {
        this.number = Integer.toString(number);
    }

    /**
     * Print.
     */
    public void print()
    {
        System.out.println(" ..... String number is " + number + "!");
    }

    /**
     * Get number.
     *
     * @return number.
     */
    public int getNumber()
    {
        return Integer.parseInt(number);
    }
}
