package javamessagingnetbeans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The consultant. Usually he/she is working in outsourcing. (PHP client for
 * example)
 */
class Consultant
{

    /*
     * Contact card.
     */
    protected Socket contactCard;

    /*
     * Interface to consultant.
     */
    protected PrintWriter toConsultant;

    /*
     * Interface to employee.
     */
    protected BufferedReader toEmployee;

    /*
     * Introduce consultant with WOW.
     */
    Wow wow;

    /**
     * The constructor.
     *
     * @param portNumber - port number.
     * @param wow - way of working.
     */
    public Consultant(int portNumber, Wow wow)
    {
        this.wow = wow;

        try
        {
            this.contactCard = (new ServerSocket(portNumber)).accept();
            this.toConsultant = new PrintWriter(this.contactCard.getOutputStream(), true);
            this.toEmployee = new BufferedReader(
                new InputStreamReader(this.contactCard.getInputStream()));
        }
        catch (IOException ex)
        {
            Logger.getLogger(Consultant.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Listen to consultant.
     *
     * @return message.
     */
    public Message listen()
    {
        String output = "";

        for (;;)
        {
            String word = null;

            try
            {
                word = this.toEmployee.readLine();
            }
            catch (IOException ex)
            {
                Logger.getLogger(Consultant.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (word == null)
            {
                break;
            }

            output += word;

            if (wow.conversationEnd(word))
            {
                break;
            }
        }

        return this.wow.decode(output);
    }

    /**
     * Tell something to a consultant.
     *
     * @param message - message.
     */
    public void tell(Message message)
    {
        String input = wow.code(message);

        if (input != null)
        {
            this.toConsultant.print(input);
        }
    }

    /**
     * Fire consultant.
     */
    public void fire()
    {
        try
        {
            this.contactCard.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Consultant.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
