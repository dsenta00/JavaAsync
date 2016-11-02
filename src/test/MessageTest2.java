package test;

public class MessageTest2
{
    private final String number;

    public MessageTest2(int number) {
        this.number = Integer.toString(number);
    }

    public void read()
    {
        System.out.print("Numba is " + this.number + "!\n");
    }

    public int getNumber()
    {
        return Integer.parseInt(this.number);
    }
}
