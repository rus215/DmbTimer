package army.org.dmbtimer.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import army.org.dmbtimer.R
import army.org.dmbtimer.activity.AddActivity
import army.org.dmbtimer.room.database.SoldierViewModel
import army.org.dmbtimer.utils.DateUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

    private lateinit var dateUtil: DateUtil
    private var disposable: Disposable? = null


    //Прошедшее время
    private var passedValue: Int = 0
    private var passedHours: Int = 0
    private var passedMinutes: Int = 0
    private var passedSeconds: Int = 0

    //Оставшееся время
    private var leftValue: Int = 0
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
            ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory(activity!!.application)).get(SoldierViewModel::class.java)
        //Проверяем есть ли солдаты в базе данных
        soldierViewModel.allSoldiers.observe(this, Observer {
            if (it.isEmpty()) {
                //Если нет запускаем окно добавление солдата
                val intent = Intent(context, AddActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                dateUtil = DateUtil(it[0].startDate)
                calculate()
            }
        })
    }

    private fun calculate() {

        //Инициализация переменных
        initVariables()

        disposable = Observable.interval(1, TimeUnit.SECONDS)
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
                        passedValue++
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
                        leftValue--
                        leftHours = 23
                    }
                }
                //Индикатор прогресса
                textProgress.text = String.format("%3.6f%%", (pastNum / totalNum) * 100)
                ringProgress.progress = pastNum / totalNum

                //Прошедшее время
                textPassedValue.text = String.format("дней: %d", passedValue)
                textPassedHours.text = String.format("часов: %02d", passedHours)
                textPassedMinutes.text = String.format("минут: %02d", passedMinutes)
                textPassedSeconds.text = String.format("секунд: %02d", passedSeconds)

                //Оставшееся время
                textLeftValue.text = String.format("дней: %d", leftValue)
                textLeftHours.text = String.format("часов: %02d", leftHours)
                textLeftMinutes.text = String.format("минут: %02d", leftMinutes)
                textLeftSeconds.text = String.format("секунд: %02d", leftSeconds)
            }


    }

    private fun initVariables() {
        //Прошло время в секундах
        pastNum = dateUtil.passedTimeInSec().seconds
        //Общее время
        totalNum = dateUtil.totalTimeInSec().seconds.toFloat()

        //Прошло
        passedValue = dateUtil.passedDays().days
        passedHours = dateUtil.passedHours().hours
        passedMinutes = dateUtil.passedMinutes().minutes
        passedSeconds = dateUtil.passedSeconds().seconds

        //Осталось
        leftValue = dateUtil.leftDays().days
        leftHours = dateUtil.leftHours().hours
        leftMinutes = dateUtil.leftMinutes().minutes
        leftSeconds = dateUtil.leftSeconds().seconds
    }

    override fun onDestroy() {
        super.onDestroy()
        //Очищаем стрим
        disposable?.dispose()

    }
}
