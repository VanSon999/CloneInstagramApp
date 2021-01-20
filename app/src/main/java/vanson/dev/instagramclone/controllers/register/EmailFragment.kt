package vanson.dev.instagramclone.controllers.register

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_register_email.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.determineStateBtn

class EmailFragment : Fragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onNext(email: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        next_btn.setOnClickListener {
            val email = email_input_register.text.toString()
            mListener.onNext(email)
        }

        determineStateBtn(next_btn, email_input_register)
    }

    override fun onAttach(context: Context) { //attach to activity, so context is this activity
        super.onAttach(context)
        mListener = context as Listener
    }
}