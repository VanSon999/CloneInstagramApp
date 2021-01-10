package vanson.dev.instagramclone.Controllers

import android.os.Bundle
import android.util.Log
import vanson.dev.instagramclone.R

class LikesActivity : BaseActivity(3) {
    private val TAG = "LikesActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation()
        Log.d(TAG, "onCreate: ${this.navNumber}")
    }

    override fun onStart() {
        super.onStart()
        setActivityChecked(3)
    }
}