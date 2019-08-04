package javaasync

import javaasync.message.CollaborationMessage

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.logging.Level
import java.util.logging.Logger

/**
 * The consultant. Usually he/she is working in outsourcing. (PHP client for
 * example)
 */
class Consultant(portNumber: Int, private var wow: Wow) {

    /*
     * Contact card.
     */
    private lateinit var contactCard: Socket

    /*
     * Interface to consultant.
     */
    private lateinit var toConsultant: PrintWriter

    /*
     * Interface to employee.
     */
    private lateinit var toEmployee: BufferedReader

    init {

        try {
            this.contactCard = ServerSocket(portNumber).accept()
            this.toConsultant = PrintWriter(this.contactCard.getOutputStream(), true)
            this.toEmployee = BufferedReader(
                InputStreamReader(this.contactCard.getInputStream())
            )
        } catch (ex: IOException) {
            Logger.getLogger(Consultant::class.java.name).log(Level.SEVERE, null, ex)
        }

    }

    /**
     * Listen to consultant.
     *
     * @return message.
     */
    fun listen(): CollaborationMessage {
        var output = ""

        while (true) {
            var word: String? = null

            try {
                word = this.toEmployee.readLine()
            } catch (ex: IOException) {
                Logger.getLogger(Consultant::class.java.name).log(Level.SEVERE, null, ex)
            }

            if (word == null) {
                break
            }

            output += word

            if (wow.conversationEnd(word)) {
                break
            }
        }

        return this.wow.decode(output)
    }

    /**
     * Tell something to a consultant.
     *
     * @param message - message.
     */
    fun tell(message: CollaborationMessage) {
        val input = wow.code(message)

        this.toConsultant.print(input)
    }

    /**
     * Fire consultant.
     */
    fun fire() {
        try {
            this.contactCard.close()
        } catch (ex: IOException) {
            Logger.getLogger(Consultant::class.java.name).log(Level.SEVERE, null, ex)
        }
    }
}
