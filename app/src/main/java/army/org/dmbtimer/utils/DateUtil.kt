package army.org.dmbtimer.utils

import org.joda.time.*
import java.util.*


class DateUtil(timeInMills: Long) {

    private val startDate: LocalDateTime
    private val finisDate: LocalDateTime
    private val nowDate: LocalDateTime

    init {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMills
        startDate = LocalDateTime.fromCalendarFields(calendar)
        finisDate = startDate.plusYears(1)
        nowDate = LocalDateTime.now()

    }

    fun passedTimeInSec(): Seconds = Seconds.secondsBetween(startDate, nowDate)
    fun totalTimeInSec(): Seconds = Seconds.secondsBetween(startDate, finisDate)

    fun passedDays(): Days = Days.daysBetween(startDate, nowDate)
    fun passedHours(): Hours = Hours.hoursBetween(startDate, nowDate).minus(passedDays().days * 24)
    fun passedMinutes(): Minutes =
        Minutes.minutesBetween(
            startDate,
            nowDate
        ).minus(passedDays().days * 24 * 60).minus(passedHours().hours * 60)

    fun passedSeconds(): Seconds =
        Seconds.secondsBetween(
            startDate,
            nowDate
        ).minus(passedDays().days * 24 * 60 * 60).minus(passedHours().hours * 60 * 60).minus(
            passedMinutes().minutes * 60
        )

    fun leftDays(): Days = Days.daysBetween(nowDate, finisDate)
    fun leftHours(): Hours = Hours.hoursBetween(nowDate, finisDate).minus(leftDays().days * 24)
    fun leftMinutes(): Minutes =
        Minutes.minutesBetween(
            nowDate,
            finisDate
        ).minus(leftDays().days * 24 * 60).minus(leftHours().hours * 60)

    fun leftSeconds(): Seconds =
        Seconds.secondsBetween(
            nowDate,
            finisDate
        ).minus(leftDays().days * 24 * 60 * 60).minus(leftHours().hours * 60 * 60).minus(
            leftMinutes().minutes * 60
        )

}