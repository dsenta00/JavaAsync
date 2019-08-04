package test

import javaasync.Competence
import javaasync.AsyncService
import javaasync.Manager
import javaasync.message.CollaborationMessage

/**
 * Employee test 3.
 */
class AsyncServiceTest3(manager: Manager, employeeName: String) : AsyncService(manager, employeeName) {

    init {
        val employee = manager.getService(AsyncServiceTest::class.simpleName.toString())
        setupCollaboration(employee)
        send(
            message = StringMessage(0),
            toAsyncService = employee
        )
        send(TmoMessage(), this, 1000)
    }

    @Competence(message = StringMessage::class)
    fun handleStringMessage(message: CollaborationMessage) {
        val info = message.content<StringMessage>()
        info.print()

        send(DummyMessage(), message.sender())

        if (info.number.toInt() > 100000) {
            manager.bankcrupt()
        } else {
            send(StringMessage(info.number.toInt() + 1), message.sender())
        }
    }

    @Competence(message = TmoMessage::class)
    fun handleDummyMessage(message: CollaborationMessage) {
        val info = message.content<TmoMessage>()
        info.print()
        send(message.content(), this, 1000)
    }
}
