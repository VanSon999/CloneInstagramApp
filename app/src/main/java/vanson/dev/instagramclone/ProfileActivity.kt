package vanson.dev.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(4) {
    private val TAG = "ProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation()
        Log.d(TAG, "onCreate: ${this.navNumber}")

        edit_profile_btn.setOnClickListener {
            val intent =Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        setActivityChecked(4)
    }
}