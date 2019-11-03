package army.org.dmbtimer.fragments.add

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import army.org.dmbtimer.R
import kotlinx.android.synthetic.main.fragment_username.*

class UserNameFragment : Fragment() {

    private var listener: OnFragmentUserTextChangeListener? = null
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userName = arguments?.getString(ARG_NAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_username, container, false)
    }

    fun onUserNameCorrect(text: String) {
        listener?.onUserTextChanged(text)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentUserTextChangeListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentUserTextChangeListener")
        }
    }

    override fun onStart() {
        super.onStart()
        editTextName?.setText(userName)

        editTextName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                onUserNameCorrect(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentUserTextChangeListener {
        fun onUserTextChanged(text: String)
    }

    companion object {
        private const val ARG_NAME = "user_name"

        @JvmStatic
        fun newInstance(userName: String?): UserNameFragment {
            val args = Bundle()
            args.putString(ARG_NAME, userName)
            val fragment = UserNameFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
