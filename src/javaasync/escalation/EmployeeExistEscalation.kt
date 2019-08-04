package javaasync.escalation

class EmployeeExistEscalation(private val name: String) : EscalationReport() {
    override fun handle() {

    }

    override fun reason(): String {
        return "Employee with name $name already exists!"
    }
}
