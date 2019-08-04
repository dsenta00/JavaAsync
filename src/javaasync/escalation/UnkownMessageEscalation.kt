/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaasync.escalation

import javaasync.message.Message

/**
 * Unknown message report.
 */
class UnkownMessageEscalation(val message: Message) : EscalationReport() {

    /**
     * Handle unknown message.
     */
    override fun handle() {
        /* No action to recover */
    }

    /**
     * Give me proper reason.
     *
     * @return string reason.
     */
    override fun reason(): String {
        return ("Message \"" + message.type()
                + "\" not supported to handle from " + message.owner()!!.name()
                + if (recovered)
            " -> RECOVERED"
        else
            " -> NOT RECOVERED")
    }

}
