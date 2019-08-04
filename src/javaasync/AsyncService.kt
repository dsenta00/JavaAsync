package javaasync

import javaasync.escalation.CollaborationExistEscalation
import javaasync.escalation.EscalationReport
import javaasync.escalation.UnknownCollaborationEscalation
import javaasync.escalation.UnkownMessageEscalation
import javaasync.message.CloseCollaborationConfirm
import javaasync.message.CloseCollaborationRequest
import javaasync.message.CollaborationMessage
import javaasync.message.Message
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberFunctions

/**
 * Employee class represents a independent worker. Each me is ran through its
 * own thread. Employees can collaborate in order to exchange messages.
 */
open class AsyncService(
    val manager: Manager,
    private var employeeName: String
) : Thread() {

    private var competenceMap: MutableMap<String, KFunction<*>> = ConcurrentHashMap()
    private var vacation: EmployeesVacation
    private val collaborationMap: MutableMap<AsyncService, Collaboration> = ConcurrentHashMap()
    private var ownTasks: Collaboration? = null
    private var leavingJob: Boolean = false

    /**
     * Constructor.
     */
    init {
        manager.asyncServiceMap[employeeName] = this
        vacation = EmployeesVacation()
        leavingJob = false
        cacheCompetenceMethods()
    }

    /**
     * Leave company.
     */
    private fun leaveCompany() {
        log("just left company")
        Thread.currentThread().stop()
    }

    /**
     * Handle message CloseCollaborationRequest.
     *
     * @param message - CloseCollaborationRequest message
     */
    @Competence(CloseCollaborationRequest::class)
    private fun handleMessageCloseCollaborationRequest(message: Message) {
        val collaboration = getCollaboration(message.sender())

        if (collaboration == null) {
            escalation(UnknownCollaborationEscalation(message.sender(), this))
            return
        }

        log("closing collaboration with " + message.sender().name()!!)

        collaboration.send(this, CloseCollaborationConfirm(this))
        collaborationMap.remove(message.sender())
    }

    /**
     * Handle message CloseCollaborationConfirm.
     *
     * @param message - CloseCollaborationConfirm message
     */
    @Competence(CloseCollaborationConfirm::class)
    private fun handleMessageCloseCollaborationConfirm(message: Message) {
        collaborationMap.remove(message.sender())

        log("closed collaboration with " + message.sender().name()!!)

        if (collaborationMap.isEmpty()) {
            /*
             * All collaborations are closed.
             * Leave company.
             */
            leaveCompany()
        }
    }

    /**
     * Setup collaboration with me.
     *
     * @param asyncService      - me to set collaboration with.
     * @param collaboration - collaboration contract to set me.
     */
    private fun offerCollaboration(asyncService: AsyncService, collaboration: Collaboration) {
        val existingCollaboration = getCollaboration(asyncService)

        if (existingCollaboration != null) {
            escalation(CollaborationExistEscalation(existingCollaboration))
            return
        }

        log("offered collaboration from " + asyncService.name()!!)

        collaborationMap[asyncService] = collaboration
    }

    protected fun <T : AsyncService> createService(clazz: KClass<T>, name: String = clazz.simpleName.toString()): T {
        val employee = manager.createService(clazz, name)

        employee.start()
        setupCollaboration(employee)

        return employee
    }

    /**
     * Setup collaboration with me.
     *
     * @param asyncService - me to set collaboration with.
     */
    fun setupCollaboration(asyncService: AsyncService) {
        var collaboration = getCollaboration(asyncService)

        if (collaboration != null) {
            escalation(CollaborationExistEscalation(collaboration))
            return
        }

        log("setup collaboration with " + asyncService.name()!!)

        collaboration = Collaboration(this, asyncService)

        collaborationMap[asyncService] = collaboration
        asyncService.offerCollaboration(this, collaboration)
    }

    /**
     * Get collaboration between another me.
     *
     * @param asyncService - another me.
     * @return Collaboration if exist, otherwise null.
     */
    private fun getCollaboration(asyncService: AsyncService): Collaboration? {
        return collaborationMap[asyncService]
    }

    /**
     * Get me by his name.
     *
     * @param name - employees name.
     * @return Employee if exist, otherwise null.
     */
    protected fun getService(name: String): AsyncService {
        return manager.getService(name)
    }

    /**
     * Send message if collaboration between employees exist. Otherwise throw
     * escalation.
     *
     * @param message    - message to send.
     * @param toAsyncService - me to send message.
     */
    fun send(message: Any, toAsyncService: AsyncService) {
        var collaboration = getCollaboration(toAsyncService)

        if (collaboration == null) {
            if (this !== toAsyncService) {
                escalation(UnknownCollaborationEscalation(this, toAsyncService))
                return
            } else {
                collaboration = ownTasks
            }
        }

        collaboration!!.send(this, CollaborationMessage(this, message))
    }

    protected fun send(message: Any, toAsyncService: AsyncService, tmo: Int) {
        val thread = Thread(Runnable {
            try {
                Thread.sleep(tmo.toLong())
            } catch (ex: InterruptedException) {
                Logger.getLogger(AsyncService::class.java.name).log(Level.SEVERE, null, ex)
            }

            this@AsyncService.send(message, toAsyncService)
        })

        thread.start()
    }

    protected fun sendToAll(message: Any) {
        for (collaboration in collaborationMap.values) {
            collaboration.send(this, CollaborationMessage(this, message))
        }
    }

    /**
     * Check if me is leaving company.
     *
     * @return true if leaving, otherwise false.
     */
    fun leavingJob(): Boolean {
        return leavingJob
    }

    /**
     * Send all collaboration request in order to quit job. If no collaboration
     * exist, leave company.
     */
    fun quitJob() {
        log("leaving company")

        leavingJob = true

        if (collaborationMap.isEmpty()) {
            leaveCompany()
            return
        }

        sendToAll(CloseCollaborationRequest(this))
    }

    /**
     * Throw a escalation.
     *
     * @param report - escalation report.
     */
    private fun escalation(report: EscalationReport) {
        manager.raiseEscalation(report)
    }

    /**
     * Relax for some time.
     *
     * @param nanoseconds - milliseconds to sleep.
     */
    private fun relax(nanoseconds: Int) {
        try {
            Thread.sleep(0, nanoseconds)
        } catch (e: InterruptedException) {
        }
    }

    /**
     * Printout me log.
     *
     * @param string - string to print
     */
    private fun log(string: String) {
        manager.secretary.log(name() + " -> " + string)
    }

    /**
     * Receive message.
     *
     * @return the CollaborationMessage.
     */
    private fun receive(): Message {
        while (true) {
            var message: Message? = null

            for (collaboration in collaborationMap.values) {
                message = collaboration.receiveMessage(this)

                if (message != null) {
                    break
                }
            }

            /*
             * If not received messages from others, check own tasks.
             */
            if (message == null) {
                message = ownTasks!!.receiveMessage(this)
            }

            if (message == null) {
                /*
                 * Nothing to do, take a vacation.
                 */
                val relaxTime = vacation.count()

                if (relaxTime > 0) {
                    relax(relaxTime)
                } else if (relaxTime == -1) {
                    manager.fireEmployee(employeeName)
                }
            } else {
                vacation.reset()

                if (!leavingJob) {
                    return message
                }
            }
        }
    }

    /**
     * Set me name.
     *
     * @param name - name.
     */
    fun name(name: String) {
        employeeName = name
    }

    /**
     * Get me name.
     *
     * @return - me name.
     */
    fun name(): String? {
        return employeeName
    }

    /**
     * Executable part of Thread.
     */
    override fun run() {
        ownTasks = Collaboration(this, this)

        while (true) {
            val message = receive()
            val method = competenceMap[message.type()]

            if (method == null) {
                escalation(UnkownMessageEscalation(message))
            } else {
                method.call(this, message)
            }
        }
    }

    private fun cacheCompetenceMethods() {
        for (method in this.javaClass.declaredMethods) {
            for (annotation in method.annotations) when (annotation) {
                is Competence -> {
                    competenceMap[annotation.message.simpleName!!] = this::class.memberFunctions.single { it.name == method.name }
                }
            }
        }
    }
}
