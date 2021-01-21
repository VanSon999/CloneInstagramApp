package vanson.dev.instagramclone

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import vanson.dev.instagramclone.utilites.formatRelativeTimestamp
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TestUtilsTest {
    private val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2021)
        set(Calendar.MONTH, 0)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val time = calendar.time
        val time10Sec = calendar.change { add(Calendar.SECOND, 10) }.time
        val time10Min = calendar.change { add(Calendar.MINUTE, 10) }.time
        val time1Hour = calendar.change { add(Calendar.HOUR_OF_DAY, 1) }.time
        val time1Day = calendar.change { add(Calendar.DAY_OF_MONTH, 1) }.time

        assertEquals("10 sec", formatRelativeTimestamp(time, time10Sec))
        assertEquals("10 min", formatRelativeTimestamp(time, time10Min))
        assertEquals("1 hr", formatRelativeTimestamp(time, time1Hour))
        assertEquals("Yesterday", formatRelativeTimestamp(time, time1Day))
    }

    private fun Calendar.change(f: Calendar.() -> Unit): Calendar {
        val newCalendar = clone() as Calendar
        f(newCalendar)
        return newCalendar
    }
}