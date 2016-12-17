package test;

public class MessageTest
{

    private final int number;

    public MessageTest(int number)
    {
        this.number = number;
    }

    public void read()
    {
        System.out.println(" ..... Number is " + number + "!");
    }

    public int getNumber()
    {
        return number;
    }
}
