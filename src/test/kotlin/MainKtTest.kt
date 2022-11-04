import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MainKtTest {

    @Test
    fun testGetExecutionTimeForTask_twoAsterisks() {
        val result = getExecutionTimeForTask(
            "* * taskName",
            Time(16, 52)
        )

        assertEquals("16:52 today - taskName", result)
    }

    @Test
    fun testGetExecutionTimeForTask_noAsterisks() {
        val result = getExecutionTimeForTask(
            "13 0 taskName",
            Time(16, 52)
        )

        assertEquals("0:13 tomorrow - taskName", result)
    }

    @Test
    fun testGetExecutionTimeForTask_hourAsterisk() {
        val result = getExecutionTimeForTask(
            "52 * taskName",
            Time(16, 52)
        )

        assertEquals("16:52 today - taskName", result)
    }

    @Test
    fun testGetExecutionTimeForTask_hourAsteriskWithOverflow() {
        val result = getExecutionTimeForTask(
            "51 * taskName",
            Time(23, 52)
        )

        assertEquals("0:51 tomorrow - taskName", result)
    }

    @Test
    fun testGetExecutionTimeForTask_minuteAsterisk() {
        val result = getExecutionTimeForTask(
            "* 18 taskName",
            Time(16, 52)
        )

        assertEquals("18:00 today - taskName", result)
    }

    @Test
    fun testGetExecutionTimeForTask_minutesRepresentationDoubleDigit() {
        val result = getExecutionTimeForTask(
            "5 17 taskName",        // make sure it's 17:05 and not 17:5
            Time(16, 52)
        )

        assertEquals("17:05 today - taskName", result)
    }

    @Test
    fun testGetExecutionTimeForTask_minutesRepresentationDoubleDigit_twoAsterisks() {
        val result = getExecutionTimeForTask(
            "* * taskName",
            Time(0, 0)
        )

        assertEquals("0:00 today - taskName", result)
    }

    @Test
    fun testGetExecutionTimeForTask_minutesRepresentationDoubleDigit_minuteAsterisk() {
        val result = getExecutionTimeForTask(
            "* 15 taskName",
            Time(15, 0)
        )

        assertEquals("15:00 today - taskName", result)
    }
}