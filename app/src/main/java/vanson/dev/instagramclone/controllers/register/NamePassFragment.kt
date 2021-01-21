package vanson.dev.instagramclone.controllers.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_register_namepass.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.determineStateBtn

class NamePassFragment : Fragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onRegister(fullname: String, password: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_namepass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        register_btn.setOnClickListener {
            val fullName = full_name_input.text.toString()
            val password = password_input.text.toString()
            mListener.onRegister(fullName, password)
        }

        determineStateBtn(register_btn, full_name_input, password_input)
    }

    override fun onAttach(context: Context) { //attach to activity, so context is this activity
        super.onAttach(context)
        mListener = context as Listener
    }
}