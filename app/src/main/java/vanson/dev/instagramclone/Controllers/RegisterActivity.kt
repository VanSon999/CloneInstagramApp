package vanson.dev.instagramclone.Controllers

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_register_email.*
import kotlinx.android.synthetic.main.fragment_register_namepass.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.determineStateBtn
import vanson.dev.instagramclone.showToast

class RegisterActivity : AppCompatActivity(), EmailFragment.Listener, NamePassFragment.Listener {
    private val TAG = "RegisterActivity"
    private var mEmail: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.frame_layout, EmailFragment())
                .commit()
        }
    }

    override fun onNext(email: String) {
        if (email.isNotEmpty()) {
            mEmail = email
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result?.signInMethods?.isEmpty() == true) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, NamePassFragment())
                            .addToBackStack(null) //save state for user back to EmailFragment
                            .commit()
                    }else{
                        showToast("This email already exists")
                    }
                } else {
                    showToast(it.exception!!.message!!)
                }
            }
        } else {
            showToast("Please enter email!")
        }
    }

    override fun onRegister(fullname: String, password: String) {
        if (fullname.isNotEmpty() && password.isNotEmpty()) {
            val email = mEmail
            if (email != null) { //check again because mEmail can be change with var!
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val user = mUser(fullname, email)
                            val reference = mDatabase.child("Users").child(it.result?.user?.uid!!)
                            reference.setValue(user).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    startHomeActivity()
                                } else {
                                    unknowRegisterError(it)
                                }
                            }
                        } else {
                            unknowRegisterError(it)
                        }
                    }
            } else {
                Log.e(TAG, "onRegister: email is null")
                showToast("Please enter email")
                supportFragmentManager.popBackStack() // back to EmailFragment
            }
        } else {
            showToast("Please enter full name and password")
        }
    }

    private fun unknowRegisterError(it: Task<*>) {
        Log.e(TAG, "onRegister: fail to create user profile", it.exception)
        showToast("Something wrong happened. Please try again later!")
    }

    private fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun mUser(fullname: String, email: String): User {
        val username = mUserName(fullname)
        return User(name = fullname, username = username, email = email)
    }

    private fun mUserName(fullname: String) = fullname.toLowerCase().replace(" ", ".")
}

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
        mListener = context as NamePassFragment.Listener
    }
}