package army.org.dmbtimer.utils

import org.joda.time.*
import java.util.*


/**
 * Утилитный класс для вычисления времени
 */
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

    /**
     * @return - Количество прошедших секунд с начала призыва до текущей даты
     */
    fun passedTimeInSec(): Seconds = Seconds.secondsBetween(startDate, nowDate)

    /**
     * @return - Количество прошедших секунд с начала призыва до ДМБ
     */
    fun totalTimeInSec(): Seconds = Seconds.secondsBetween(startDate, finisDate)

    /**
     * @return - Количество прошедших дней с начала призыва до текущей даты
     */
    fun passedDays(): Days = Days.daysBetween(startDate, nowDate)

    /**
     * @return - Количество прошедших часов с начала призыва до текущей даты с учетом дней
     */
    fun passedHours(): Hours = Hours.hoursBetween(startDate, nowDate).minus(passedDays().days * 24)

    /**
     * @return - Количество прошедших минут с начала призыва до текущей даты с учетом дней и часов
     */
    fun passedMinutes(): Minutes =
        Minutes.minutesBetween(
            startDate,
            nowDate
        ).minus(passedDays().days * 24 * 60).minus(passedHours().hours * 60)

    /**
     * @return - Количество прошедших секунд с начала призыва до текущей даты с учетом дней, часов и минут
     */
    fun passedSeconds(): Seconds =
        Seconds.secondsBetween(
            startDate,
            nowDate
        ).minus(passedDays().days * 24 * 60 * 60).minus(passedHours().hours * 60 * 60).minus(
            passedMinutes().minutes * 60
        )

    /**
     * @return - Количество оставшихся дней с текущей даты до ДМБ
     */
    fun leftDays(): Days = Days.daysBetween(nowDate, finisDate)

    /**
     * @return - Количество оставшихся часов с текущей даты до ДМБ с учетом дней
     */
    fun leftHours(): Hours = Hours.hoursBetween(nowDate, finisDate).minus(leftDays().days * 24)

    /**
     * @return - Количество оставшихся минут с текущей даты до ДМБ с учетом дней и часов
     */
    fun leftMinutes(): Minutes =
        Minutes.minutesBetween(
            nowDate,
            finisDate
        ).minus(leftDays().days * 24 * 60).minus(leftHours().hours * 60)

    /**
     * @return - Количество оставшихся секунд с текущей даты до ДМБ с учетом дней, часов и минут
     */
    fun leftSeconds(): Seconds =
        Seconds.secondsBetween(
            nowDate,
            finisDate
        ).minus(leftDays().days * 24 * 60 * 60).minus(leftHours().hours * 60 * 60).minus(
            leftMinutes().minutes * 60
        )

}