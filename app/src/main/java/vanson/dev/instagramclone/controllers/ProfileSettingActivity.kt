package vanson.dev.instagramclone.controllers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile_settings.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.utilites.FirebaseHelper

class ProfileSettingActivity : AppCompatActivity() {
    private lateinit var mFirebase : FirebaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)
        mFirebase = FirebaseHelper(this)
        sign_out_text.setOnClickListener { mFirebase.auth.signOut()}
        back_image.setOnClickListener { finish() }
    }
}
