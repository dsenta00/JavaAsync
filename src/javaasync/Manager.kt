package javaasync

import javaasync.escalation.EmployeeExistEscalation
import javaasync.escalation.EscalationManager
import javaasync.escalation.EscalationReport
import javaasync.escalation.UnexpectedCompanyClosing

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * class Manager creates and manages employees.
 */
class Manager {

    /*
     * Employee map.
     */
    val asyncServiceMap: MutableMap<String, AsyncService> = HashMap()

    /*
     * Every handsome manager needs to have a secretary.
     */
    val secretary: Secretary = Secretary(
        SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
            .format(Date()) + ".log"
    )

    /*
     * Frankesteins sidekick - Igor.
     */
    private val escalationManager: EscalationManager = EscalationManager(this)

    /**
     * Constructor.
     */
    init {
        Runtime.getRuntime().addShutdownHook(Thread(Runnable {
            this@Manager.raiseEscalation(UnexpectedCompanyClosing(this@Manager))
            this@Manager.secretary.giveLayOffPay()
        }))
    }

    /**
     * Raise escalation.
     *
     * @param report - escalation report.
     */
    fun raiseEscalation(report: EscalationReport) {
        this.escalationManager.handle(report)
    }

    /**
     * Put traces on.
     */
    fun traceOn() {
        Trace.on()
    }

    /**
     * Put traces off.
     */
    fun traceOff() {
        Trace.off()
    }

    /**
     * Turn secretary on.
     */
    fun secretaryOn() {
        secretary.on()
    }

    /**
     * Turn secretary off.
     */
    fun secretaryOff() {
        secretary.off()
    }

    /**
     * Manager is always waiting for some employee.
     *
     * @param nanoseconds - nanoseconds to wait.
     */
    private fun wait(nanoseconds: Int) {
        try {
            Thread.sleep(0, nanoseconds)
        } catch (ex: InterruptedException) {
        }
    }

    fun getService(name: String): AsyncService {
        return asyncServiceMap[name]!!
    }

    /**
     * Get number of employees.
     *
     * @return number of employees.
     */
    fun numOfEmployees(): Int {
        return asyncServiceMap.size
    }

    /**
     * Remove employee.
     *
     * @param asyncService - employee to remove
     */
    private fun removeEmployee(asyncService: AsyncService) {
        asyncServiceMap.remove(asyncService.name())
    }

    /**
     * Fire employee.
     *
     * @param name - employee name.
     * @return true if employee quit job successfully, otherwise return false.
     */
    fun fireEmployee(name: String): Boolean {
        val employee = getService(name) ?: return false

        removeEmployee(employee)

        if (numOfEmployees() == 0) {
            closeCompany()
        } else {
            employee.quitJob()
        }

        return true
    }

    /**
     * Close company.
     */
    private fun closeCompany() {
        secretary.log("Closed company!")
        secretary.giveLayOffPay()
        Runtime.getRuntime().halt(0)
    }

    /**
     * Fire all employees and terminate application.
     */
    fun bankcrupt() {
        val employeeList = LinkedList(
            asyncServiceMap.keys
        )

        for (employeeName in employeeList) {
            fireEmployee(employeeName)
        }

        closeCompany()
    }

    fun <T : AsyncService> createService(clazz: KClass<T>, name: String = clazz.simpleName!!): T {
        return try {
            val employee = getService(name)
            this.raiseEscalation(EmployeeExistEscalation(name))
            employee as T
        } catch (exception : KotlinNullPointerException) {
            clazz.primaryConstructor?.call(this, name)!!
        }
    }
}
