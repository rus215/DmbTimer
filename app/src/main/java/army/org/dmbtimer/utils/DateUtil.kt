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
    fun passedTimeInSec(): Int = Seconds.secondsBetween(startDate, nowDate).seconds

    /**
     * @return - Количество прошедших секунд с начала призыва до ДМБ
     */
    fun totalTimeInSec(): Int = Seconds.secondsBetween(startDate, finisDate).seconds

    /**
     * @param diffDays - true, если необходимы дни с учетом месяцев и недель,
     * false для общего количества дней
     *
     * @return - Количество прошедших дней
     */
    fun passedDays(diffDays: Boolean = false): Int =
        if (diffDays) {
            Period(startDate, nowDate).days
        } else {
            Days.daysBetween(startDate, nowDate).days
        }

    /**
     * @return - Количество прошедших месяцев
     */
    fun passedMonths(): Int = Period(startDate, nowDate).months

    /**
     * @param diffWeeks - true, если необходимы недели с учетом месяцев и дней,
     * false для общего количества недель
     *
     * @return - Количество прошедших недель
     */
    fun passedWeeks(diffWeeks: Boolean = false): Int =
        if (diffWeeks) {
            Period(startDate, nowDate).weeks
        } else {
            Weeks.weeksBetween(startDate, nowDate).weeks
        }

    /**
     * @return - Количество прошедших часов с начала призыва до текущей даты с учетом дней
     */
    fun passedHours(): Int = Hours.hoursBetween(startDate, nowDate).minus(passedDays() * 24).hours

    /**
     * @return - Количество прошедших минут с начала призыва до текущей даты с учетом дней и часов
     */
    fun passedMinutes(): Int =
        Minutes.minutesBetween(
            startDate,
            nowDate
        ).minus(passedDays() * 24 * 60).minus(passedHours() * 60).minutes

    /**
     * @return - Количество прошедших секунд с начала призыва до текущей даты с учетом дней, часов и минут
     */
    fun passedSeconds(): Int =
        Seconds.secondsBetween(
            startDate,
            nowDate
        ).minus(passedDays() * 24 * 60 * 60).minus(passedHours() * 60 * 60).minus(
            passedMinutes() * 60
        ).seconds

    /**
     * @param diffDays - true, если необходимы дни с учетом месяцев и недель,
     * false для общего количества дней
     *
     * @return - Количество оставшихся дней с текущей даты до ДМБ
     */
    fun leftDays(diffDays: Boolean = false): Int =
        if (diffDays) {
            Period(nowDate, finisDate).days
        } else {
            Days.daysBetween(nowDate, finisDate).days
        }

    /**
     * @return - Количество оставшихся месяцев
     */
    fun leftMonths(): Int = Period(nowDate, finisDate).months

    /**
     * @param diffWeeks - true, если необходимы недели с учетом месяцев и дней,
     * false для общего количества недель
     *
     * @return - Количество оставшихся недель
     */
    fun leftWeeks(diffWeeks: Boolean = false): Int =
        if (diffWeeks) {
            Period(nowDate, finisDate).weeks
        } else {
            Weeks.weeksBetween(nowDate, finisDate).weeks
        }

    /**
     * @return - Количество оставшихся часов с текущей даты до ДМБ с учетом дней
     */
    fun leftHours(): Int = Hours.hoursBetween(nowDate, finisDate).minus(leftDays() * 24).hours

    /**
     * @return - Количество оставшихся минут с текущей даты до ДМБ с учетом дней и часов
     */
    fun leftMinutes(): Int =
        Minutes.minutesBetween(
            nowDate,
            finisDate
        ).minus(leftDays() * 24 * 60).minus(leftHours() * 60).minutes

    /**
     * @return - Количество оставшихся секунд с текущей даты до ДМБ с учетом дней, часов и минут
     */
    fun leftSeconds(): Int =
        Seconds.secondsBetween(
            nowDate,
            finisDate
        ).minus(leftDays() * 24 * 60 * 60).minus(leftHours() * 60 * 60).minus(
            leftMinutes() * 60
        ).seconds
}