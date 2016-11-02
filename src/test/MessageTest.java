package test;
public class MessageTest
{
    private final int number;

    public MessageTest(int number) {
        this.number = number;
    }

    public void read()
    {
        System.out.print("Number is " + this.number + "!\n");
    }

    public int getNumber()
    {
        return this.number;
    }
}
