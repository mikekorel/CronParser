import java.lang.NumberFormatException

fun main(args: Array<String>) {

    val currentTime = args[0]       // HH:MM format

    var config = ""
    do {
        val stdin = readLine()?.plus("\n")
        config += stdin ?: ""
    } while (stdin != null)

    getSchedule(currentTime, config)
}

fun getSchedule(currentTime: String, taskList: String) {
    val currTimeFields = currentTime.split(':')
    if (currTimeFields.size != 2) {
        println("Invalid input for current time!")
        return
    }
    val currTime = try {
        Time(hour = currTimeFields[0].toInt(), min = currTimeFields[1].toInt())
    } catch (e: NumberFormatException) {
        println("Invalid input for current time!")
        return
    }

    val tasks = taskList.trim().lines()
    var result = ""
    tasks.forEach { taskCommand ->
        result = result.plus(getExecutionTimeForTask(taskCommand, currTime)).plus("\n")
    }
    println(result)
}

fun getExecutionTimeForTask(task: String, currTime: Time): String {
    val fields = task.split(' ')
    if (fields.size != 3) {
        return "<Invalid input for task: $task>"
    }
    val taskTime = TimeCron(hour = fields[1], min = fields[0].makeDoubleDigit())
    val taskName = fields[2]

    /**
     * We have 4 cases for the task time formatting:
     *      *:*, HH:MM, HH:*, *:MM
     */
    val executionTime = when {

        // *:* format
        taskTime.hour == "*" && taskTime.min == "*" -> {
            // will execute immediately, just return the current time
            "${currTime.hour}:${currTime.min} today"
        }

        // HH:MM format
        taskTime.hour != "*" && taskTime.min != "*" -> {
            // will always execute at taskTime, either today or tomorrow
            val day = if (
                taskTime.hour.toInt() > currTime.hour ||
                (taskTime.hour.toInt() == currTime.hour && taskTime.min.toInt() >= currTime.min)
            ) {
                "today"     // task time is after current time
            } else {
                "tomorrow"  // task time is before current time
            }
            "${taskTime.hour}:${taskTime.min} $day"
        }

        // HH:* format
        taskTime.min == "*" -> {
            if (taskTime.hour.toInt() < currTime.hour)      // task hour before current hour
                "${taskTime.hour}:00 tomorrow"
            else if (taskTime.hour.toInt() == currTime.hour)    // task hour same as current hour
                "${taskTime.hour}:${currTime.min} today"
            else                                // task hour after current hour
                "${taskTime.hour}:00 today"
        }

        // *:MM format
        else -> {
            if (taskTime.min.toInt() >= currTime.min)   // task min not less than current min
                "${currTime.hour}:${taskTime.min} today"
            else {      // task min less than current min --> increase the hour, take care of overflows(!)
                var day = "today"
                var increasedHour = currTime.hour + 1
                if (increasedHour == 24) {      // overflow --> go back to 0 hour & change day to tomorrow
                    increasedHour = 0
                    day = "tomorrow"
                }
                "$increasedHour:${taskTime.min} $day"
            }
        }
    }

    return "$executionTime - $taskName"
}

data class Time(
    val hour: Int,
    val min: Int
)

data class TimeCron(
    val hour: String,
    val min: String
)

fun String.makeDoubleDigit() = if (this != "*")
    "0$this".takeLast(2)
else
    this

