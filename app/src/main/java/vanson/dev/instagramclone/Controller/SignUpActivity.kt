package vanson.dev.instagramclone.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_up.*
import vanson.dev.instagramclone.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signIn_link_btn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
    }
}