package vanson.dev.instagramclone.controllers.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.register.RegisterActivity
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.common.determineStateBtn
import vanson.dev.instagramclone.controllers.common.setupAuthGuard
import vanson.dev.instagramclone.controllers.home.HomeActivity

class LoginActivity : BaseActivity(), KeyboardVisibilityEventListener, View.OnClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        KeyboardVisibilityEvent.setEventListener(this, this)

        login_btn.setOnClickListener(this)
        determineStateBtn(login_btn, email_login, password_login)
        create_account_text.setOnClickListener(this)
        Log.d(tag, "onCreate: LoginActivity")
        setupAuthGuard {
            mViewModel = initViewModel()
            mViewModel.gotoHomeScreen.observe(this, Observer {
                it.getContentIfNotHandled()?.let {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
            })
            mViewModel.gotoRegisterScreen.observe(this, Observer{
                it.getContentIfNotHandled()?.let {
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
            })
            mAuth = FirebaseAuth.getInstance()
        }
    }

    override fun onVisibilityChanged(isOpen: Boolean) {
        if (isOpen) {
            create_account_text.visibility = View.GONE
        } else {
            create_account_text.visibility = View.VISIBLE
        }
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.login_btn ->
                mViewModel.onLoginClick(
                    email = email_login.text.toString(),
                    password = password_login.text.toString()
                )
            R.id.create_account_text -> {
                mViewModel.onRegisterClick()
            }
        }
    }

    companion object {
        private val tag = "LoginActivity"
    }
}