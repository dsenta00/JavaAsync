package test

import javaasync.Competence
import javaasync.AsyncService
import javaasync.Manager
import javaasync.message.CollaborationMessage

/**
 * Employee test 2.
 */
class AsyncServiceTest2(manager: Manager, employeeName: String) : AsyncService(manager, employeeName) {

    private var employeeTest3: AsyncServiceTest3 = createService(AsyncServiceTest3::class)

    init {
        send(StringMessage(1), employeeTest3)
    }

    @Competence(message = IntegerMessage::class)
    fun handleIntegerMessage(message: CollaborationMessage) {
        val info = message.content<IntegerMessage>()
        info.print()
        send(IntegerMessage(info.number + 1), message.sender())
    }

    @Competence(message = StringMessage::class)
    fun handleStringMessage(message: CollaborationMessage) {
        val info = message.content<StringMessage>()
        info.print()
        send(StringMessage(info.number.toInt() + 1), message.sender())
    }

    @Competence(message = DummyMessage::class)
    fun handleDummyMessage(message: CollaborationMessage) {
        val info = message.content<DummyMessage>()
        info.print()
    }
}
