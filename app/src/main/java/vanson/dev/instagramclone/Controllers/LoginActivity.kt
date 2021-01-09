package vanson.dev.instagramclone.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.determineStateBtn
import vanson.dev.instagramclone.showToast

class LoginActivity : AppCompatActivity(), KeyboardVisibilityEventListener, View.OnClickListener {
    private val TAG = "LoginActivity"
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        KeyboardVisibilityEvent.setEventListener(this, this)
        mAuth = FirebaseAuth.getInstance()

        login_btn.setOnClickListener(this)
        determineStateBtn(login_btn, email_login, password_login)
        create_account_text.setOnClickListener(this)
    }

    override fun onVisibilityChanged(isKeyBoardOpen: Boolean) {
        if (isKeyBoardOpen) {
            create_account_text.visibility = View.GONE
        } else {
            create_account_text.visibility = View.VISIBLE
        }
    }


    override fun onClick(view: View) {

        when (view.id) {
            R.id.login_btn -> {
                val email = email_login.text.toString()
                val password = password_login.text.toString()
                if (validate(email, password)) {
                    mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                startActivity(
                                    Intent(
                                        this,
                                        HomeActivity::class.java
                                    )
                                )
                                finish()
                            } else {
                                showToast("Something was wrong! Please check it and try again!")
//                        Toast.makeText(this, "Something was wrong! Please check it and try again!",Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    showToast("Please enter email and password!")
//            Toast.makeText(this, "Please enter email and password!", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.create_account_text -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    private fun validate(email: String, password: String) =
        email.isNotEmpty() && password.isNotEmpty()
}