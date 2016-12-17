package test;

public class MessageTest2
{

    private final String number;

    public MessageTest2(int number)
    {
        this.number = Integer.toString(number);
    }

    public void read()
    {
        System.out.println(" ..... String number is " + number + "!");
    }

    public int getNumber()
    {
        return Integer.parseInt(number);
    }
}
