/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaasync.escalation;

import javaasync.message.MessageBase;

/**
 * Unknown message report.
 */
public class UnkownMessageEscalation extends EscalationReport
{

    /*
     * The message.
     */
    private final MessageBase message;

    /**
     * The constructor.
     *
     * @param message - the message.
     */
    public UnkownMessageEscalation(MessageBase message)
    {
        this.message = message;
    }

    /**
     * Get message.
     *
     * @return message.
     */
    public MessageBase getMessage()
    {
        return message;
    }

    /**
     * Handle unknown message.
     */
    @Override
    public void handle()
    {
        /* No action to recover */
    }

    /**
     * Give me proper reason.
     *
     * @return string reason.
     */
    @Override
    public String reason()
    {
        return "Message \"" + message.type()
            + "\" not supported to handle from " + message.owner().name()
            + ((recovered)
                ? " -> RECOVERED"
                : " -> NOT RECOVERED");
    }

}
