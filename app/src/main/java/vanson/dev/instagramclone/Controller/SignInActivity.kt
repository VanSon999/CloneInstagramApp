package vanson.dev.instagramclone.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_in.*
import vanson.dev.instagramclone.R

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signUp_link_btn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }
}