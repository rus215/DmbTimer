package army.org.dmbtimer.fragments.add.soldier

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import army.org.dmbtimer.R
import kotlinx.android.synthetic.main.fragment_user_date.*
import java.util.*

class UserDateFragment : Fragment() {
    private var listener: OnFragmentDateChangedListener? = null
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_date, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val year = arguments!!.getInt(ARG_YEAR)
        val month = arguments!!.getInt(ARG_MONTH)
        val day = arguments!!.getInt(ARG_DAY)
        calendar.set(year, month, day, 0, 0, 0)
    }

    override fun onStart() {
        super.onStart()
        datePicker.init(
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            calendar.set(year, month, day)
            onDateChanged(calendar)
        }
    }

    private fun onDateChanged(calendar: Calendar) {
        listener?.onDateChanged(calendar)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentDateChangedListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnUserChoosePhotoListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentDateChangedListener {
        fun onDateChanged(calendar: Calendar)
    }

    companion object {
        private const val ARG_DAY = "user_day"
        private const val ARG_MONTH = "user_month"
        private const val ARG_YEAR = "user_year"

        @JvmStatic
        fun newInstance(calendar: Calendar): UserDateFragment {
            val args = Bundle()
            args.putInt(ARG_DAY, calendar.get(Calendar.DAY_OF_MONTH))
            args.putInt(ARG_MONTH, calendar.get(Calendar.MONTH))
            args.putInt(ARG_YEAR, calendar.get(Calendar.YEAR))
            val fragment = UserDateFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
