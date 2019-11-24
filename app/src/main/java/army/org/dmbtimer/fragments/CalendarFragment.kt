package army.org.dmbtimer.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import army.org.dmbtimer.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onStart() {
        super.onStart()

        //Настраиваем календарь
        val calendar:Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        calendarView.setDate(calendar)
    }
}
