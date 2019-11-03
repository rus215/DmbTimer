package army.org.dmbtimer.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import army.org.dmbtimer.R
import army.org.dmbtimer.fragments.add.UserDateFragment
import army.org.dmbtimer.fragments.add.UserNameFragment
import army.org.dmbtimer.fragments.add.UserPhotoFragment
import army.org.dmbtimer.room.database.Soldier
import army.org.dmbtimer.room.database.SoldierViewModel
import kotlinx.android.synthetic.main.activity_content_add.*
import java.util.*


class AddActivity : AppCompatActivity(), UserNameFragment.OnFragmentUserTextChangeListener,
    UserDateFragment.OnFragmentDateChangedListener, UserPhotoFragment.OnUserChoosePhotoListener {
    override fun onPhotoChoose(file: String) {
        userPhoto = file
    }

    override fun onDateChanged(calendar: Calendar) {
        userDate.timeInMillis = calendar.timeInMillis
    }


    private var currStep = STEP_1

    private var userName: String? = null
    private var userPhoto: String? = null
    private var userDate: Calendar = Calendar.getInstance()

    init {
        userDate.set(Calendar.HOUR_OF_DAY, 0)
        userDate.set(Calendar.MINUTE, 0)
        userDate.set(Calendar.SECOND, 0)
    }

    override fun onUserTextChanged(text: String) {
        userName = text
        btnNext.isEnabled = userName!!.trim().isNotEmpty()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_add)

        val soldierViewModel: SoldierViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(SoldierViewModel::class.java)

        setStep(currStep)

        btnNext.setOnClickListener {
            if (currStep == STEP_3) {
                val soldier = Soldier(name = userName!!, userImage = userPhoto, startDate = userDate.timeInMillis)
                soldierViewModel.insert(soldier)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                currStep++
                stepView.go(currStep, true)
                if (currStep > STEP_1)
                    btnPrevious.visibility = View.VISIBLE
                setStep(currStep)
            }
        }
        btnPrevious.setOnClickListener {
            currStep--
            stepView.go(currStep, true)
            if (currStep == STEP_1)
                btnPrevious.visibility = View.GONE
            setStep(currStep, false)
        }

    }

    private fun setStep(step: Int, swipeToRight: Boolean = true) {
        var currFragment: Fragment? = null
        btnNext.text = if (currStep == STEP_3) "готово" else "далее"

        when (step) {
            STEP_1 -> currFragment = UserNameFragment.newInstance(userName)
            STEP_2 -> {
                currFragment = UserDateFragment.newInstance(userDate)
            }

            STEP_3 -> currFragment = UserPhotoFragment.getInstance(userPhoto)
        }

        val ft = supportFragmentManager.beginTransaction()
        if (swipeToRight)
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
        else
            ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
        ft.replace(R.id.fragment, currFragment!!)
        ft.commit()
    }

    companion object {
        private const val STEP_1 = 0
        private const val STEP_2 = 1
        private const val STEP_3 = 2
    }

}
