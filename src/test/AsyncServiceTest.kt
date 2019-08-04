package test

import javaasync.Competence
import javaasync.AsyncService
import javaasync.Manager
import javaasync.message.CollaborationMessage

/**
 * Employee test.
 */
class AsyncServiceTest(manager: Manager, employeeName: String) : AsyncService(manager, employeeName) {
    init {
        val employeeTest2 = createService(AsyncServiceTest2::class)

        send(
            message = IntegerMessage(2),
            toAsyncService = employeeTest2
        )
    }

    @Competence(message = IntegerMessage::class)
    fun handleIntegerMessage(message: CollaborationMessage) {
        val info = message.content<IntegerMessage>()
        info.print()

        send(
            message = IntegerMessage(info.number + 1),
            toAsyncService = message.sender()
        )
    }

    @Competence(message = StringMessage::class)
    fun handleStringMessage(message: CollaborationMessage) {
        val info: StringMessage = message.content()
        info.print()

        send(
            message = StringMessage(info.number.toInt() + 1),
            toAsyncService = message.sender()
        )
    }

    @Competence(message = DummyMessage::class)
    fun handleDummyMessage(message: CollaborationMessage) {
        val info: DummyMessage = message.content()
        info.print()
    }
}
