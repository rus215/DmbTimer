package army.org.dmbtimer.fragments


import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import army.org.dmbtimer.R
import army.org.dmbtimer.activity.AddActivity
import army.org.dmbtimer.room.database.SoldierViewModel
import army.org.dmbtimer.utils.DateUtil
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

    private lateinit var dateUtil: DateUtil
    private var disposable: CompositeDisposable = CompositeDisposable()


    //Прошедшее время
    private var passedDays: Int = 0
    private var passedWeeks: Int = 0
    private var passedMonths: Int = 0
    private var passedHours: Int = 0
    private var passedMinutes: Int = 0
    private var passedSeconds: Int = 0

    //Оставшееся время
    private var leftDays: Int = 0
    private var leftWeeks: Int = 0
    private var leftMonths: Int = 0
    private var leftHours: Int = 0
    private var leftMinutes: Int = 0
    private var leftSeconds: Int = 0

    private var pastNum: Int = 0
    private var totalNum: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onStart() {
        super.onStart()

        val soldierViewModel: SoldierViewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory(activity!!.application)
            ).get(SoldierViewModel::class.java)
        //Проверяем есть ли солдаты в базе данных
        soldierViewModel.allSoldiers.observe(this, Observer {
            if (it.isEmpty()) {
                //Если нет запускаем окно добавление солдата
                val intent = Intent(context, AddActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                textName.text = it[0].name
                dateUtil = DateUtil(it[0].startDate)
                calculate()
                it[0].userImage?.let { userImage ->
                    disposable.add(Single.just(userImage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { uri, _ ->
                            userPhoto.setImageURI(Uri.parse(uri))
                        })
                }

            }
        })

        //Инициализируем chips
        initChips()
    }

    private fun initChips() {


        val progressChipOnClickListener = View.OnClickListener {
            (it as CompoundButton).isChecked = true

            when (it.id) {
                R.id.chipDays, R.id.chipMonthDays -> {
                    //Убираем галочку с statisticChipGroup2
                    statisticChipGroup2.clearCheck()
                }
                R.id.chipMonthsWeeksAndDays, R.id.chipWeeksDays -> {
                    //Убираем галочку с statisticChipGroup1
                    statisticChipGroup1.clearCheck()
                }
            }
        }

        //Chip progress
        chipPercent.setOnClickListener(progressChipOnClickListener)
        chipSeconds.setOnClickListener(progressChipOnClickListener)
        chipMinutes.setOnClickListener(progressChipOnClickListener)
        chipHours.setOnClickListener(progressChipOnClickListener)

        //Chip statistic1
        chipDays.setOnClickListener(progressChipOnClickListener)
        chipMonthDays.setOnClickListener(progressChipOnClickListener)

        //Chip statistic2
        chipMonthsWeeksAndDays.setOnClickListener(progressChipOnClickListener)
        chipWeeksDays.setOnClickListener(progressChipOnClickListener)
    }

    private fun calculate() {

        //Инициализация переменных
        initVariables()
        //Инициализация диаграммы
        initDiagram()


        disposable.add(Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                pastNum++
                passedSeconds++
                leftSeconds--

                when {
                    passedSeconds > 59 -> {
                        passedSeconds = 0
                        passedMinutes++
                    }
                    passedMinutes > 59 -> {
                        passedMinutes = 0
                        passedHours++
                    }
                    passedHours > 23 -> {
                        passedHours = 0
                    }
                }

                when {
                    leftSeconds < 0 -> {
                        leftSeconds = 59
                        leftMinutes--
                    }
                    leftMinutes < 0 -> {
                        leftMinutes = 59
                        leftHours--
                    }
                    leftHours < 0 -> {
                        leftHours = 23
                    }
                }
                //Индикатор прогресса
                if (buttonLeft.isChecked) {
                    when (progressChipGroup.checkedChipId) {
                        R.id.chipPercent -> {
                            textProgress.text =
                                String.format("%3.6f %%", 100 - (pastNum / totalNum) * 100)
                        }

                        R.id.chipSeconds -> {
                            textProgress.text =
                                String.format(
                                    "%.0f сек.\nиз\n%.0f",
                                    totalNum - pastNum,
                                    totalNum
                                )
                        }

                        R.id.chipMinutes -> {
                            val pMin = totalNum / 60
                            textProgress.text =
                                String.format("%.0f мин.\nиз\n%.0f", pMin - pastNum / 60, pMin)
                        }

                        R.id.chipHours -> {
                            val pHours = totalNum / 3600
                            textProgress.text =
                                String.format(
                                    "%.0f ч.\nиз\n%.0f",
                                    pHours - pastNum / 3600,
                                    pHours
                                )

                        }
                    }
                } else {
                    when (progressChipGroup.checkedChipId) {
                        R.id.chipPercent -> {
                            textProgress.text =
                                String.format("%3.6f %%", (pastNum / totalNum) * 100)
                        }

                        R.id.chipSeconds -> {
                            textProgress.text =
                                String.format("%d сек.\nиз\n%.0f", pastNum, totalNum)
                        }

                        R.id.chipMinutes -> {
                            textProgress.text =
                                String.format("%d мин.\nиз\n%.0f", pastNum / 60, totalNum / 60)
                        }

                        R.id.chipHours -> {
                            textProgress.text =
                                String.format(
                                    "%d ч.\nиз\n%.0f",
                                    pastNum / 3600,
                                    totalNum / 3600
                                )

                        }
                    }
                }

                //Рассчет даты и времени
                val checkedId: Int = if (statisticChipGroup2.checkedChipId != -1) {
                    statisticChipGroup2.checkedChipId
                } else {
                    statisticChipGroup1.checkedChipId
                }

                when (checkedId) {
                    R.id.chipDays -> {

                        passedDays = dateUtil.passedDays()
                        //Прошедшие дни
                        textPassedValue.text = String.format("дней: %d", passedDays)

                        leftDays = dateUtil.leftDays()
                        //Оставшееся дни
                        textLeftValue.text = String.format("дней: %d", leftDays)
                    }

                    R.id.chipMonthDays -> {

                        passedMonths = dateUtil.passedMonths()
                        passedDays = dateUtil.passedDays(true) + dateUtil.passedWeeks(true) * 7
                        //Прошедшие месяцы и дни
                        textPassedValue.text =
                            String.format("месяцев: %d\nдней: %d", passedMonths, passedDays)

                        leftMonths = dateUtil.leftMonths()
                        leftDays = dateUtil.leftDays(true) + dateUtil.leftWeeks(true) * 7
                        //Оставшееся месяцы и дни
                        textLeftValue.text =
                            String.format("месяцев: %d\nдней: %d", leftMonths, leftDays)
                    }

                    R.id.chipMonthsWeeksAndDays -> {

                        passedMonths = dateUtil.passedMonths()
                        passedWeeks = dateUtil.passedWeeks(true)
                        passedDays = dateUtil.passedDays(true)
                        //Прошедшие месяцы и недели
                        textPassedValue.text =
                            String.format(
                                "месяцев: %d\nнедель: %d\nдней: %d",
                                passedMonths,
                                passedWeeks,
                                passedDays
                            )

                        leftMonths = dateUtil.leftMonths()
                        leftWeeks = dateUtil.leftWeeks(true)
                        leftDays = dateUtil.leftDays(true)
                        //Оставшееся месяцы и недели
                        textLeftValue.text =
                            String.format(
                                "месяцев: %d\nнедель: %d\nдней: %d",
                                leftMonths,
                                leftWeeks,
                                leftDays
                            )
                    }

                    R.id.chipWeeksDays -> {

                        passedWeeks = dateUtil.passedWeeks()
                        passedDays = dateUtil.passedDays() - passedWeeks * 7
                        //Прошедшие недели и дни
                        textPassedValue.text =
                            String.format("недель: %d\nдней: %d", passedWeeks, passedDays)

                        leftWeeks = dateUtil.leftWeeks()
                        leftDays = dateUtil.leftDays() - leftWeeks * 7
                        //Оставшееся недели и дни
                        textLeftValue.text =
                            String.format("недель: %d\nдней: %d", leftWeeks, leftDays)
                    }
                }

                //Прошедшедшее время
                textPassedHours.text = String.format("часов: %02d", passedHours)
                textPassedMinutes.text = String.format("минут: %02d", passedMinutes)
                textPassedSeconds.text = String.format("секунд: %02d", passedSeconds)

                //Оставшееся время
                textLeftHours.text = String.format("часов: %02d", leftHours)
                textLeftMinutes.text = String.format("минут: %02d", leftMinutes)
                textLeftSeconds.text = String.format("секунд: %02d", leftSeconds)
            })

    }


    private fun initVariables() {
        //Прошло время в секундах
        pastNum = dateUtil.passedTimeInSec()
        //Общее время
        totalNum = dateUtil.totalTimeInSec().toFloat()

        //Прошло
        passedHours = dateUtil.passedHours()
        passedMinutes = dateUtil.passedMinutes()
        passedSeconds = dateUtil.passedSeconds()

        //Осталось
        leftHours = dateUtil.leftHours()
        leftMinutes = dateUtil.leftMinutes()
        leftSeconds = dateUtil.leftSeconds()
    }

    private fun initDiagram() {
        //Инициализация диаграммы
        val pastPercent = pastNum / totalNum
        val items = listOf(PieEntry(pastPercent, "Прошло"), PieEntry(1 - pastPercent, "Осталось"))
        val pieDataSet = PieDataSet(items, "")
        pieDataSet.setDrawIcons(false)

        //Разделение диаграммы
        pieDataSet.sliceSpace = 3f
        //Убираеи возможность выбора
        pieDataSet.selectionShift = 0f

        //Настраиваем цвета
        pieDataSet.colors = listOf(ColorTemplate.rgb("#1b5e20"), ColorTemplate.rgb("#304ffe"))

        //Настраиваем значения, выбирвем проценты
        val pieData = PieData(pieDataSet)
        pieData.setValueTextColor(Color.WHITE)
        pieData.setValueFormatter(PercentFormatter(diagram))

        //Добавляем данные на диаграмму
        diagram.data = pieData
        diagram.setUsePercentValues(true)

        //Отключение легенды
        diagram.legend.isEnabled = false

        //Коэффциент вращения
        diagram.dragDecelerationFrictionCoef = 0.95f

        //Привращаем диаграмму в кольцевую
        diagram.isDrawHoleEnabled = true
        diagram.setHoleColor(Color.TRANSPARENT)
        diagram.setTransparentCircleColor(Color.WHITE)
        diagram.setTransparentCircleAlpha(110)
        diagram.holeRadius = 50f
        diagram.transparentCircleRadius = 61f

        //Изначальный угол
        diagram.rotationAngle = 0F
        //Разрешить вращение
        diagram.isRotationEnabled = true
        //Убираем описание диаграммы
        diagram.description.isEnabled = false

        //Обновляем диаграмму
        diagram.invalidate()
    }

    override fun onDestroy() {
        super.onDestroy()
        //Очищаем стрим
        disposable.dispose()
    }
}
