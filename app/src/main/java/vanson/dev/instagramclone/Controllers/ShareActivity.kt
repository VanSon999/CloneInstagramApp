package vanson.dev.instagramclone.Controllers

import android.os.Bundle
import android.util.Log
import vanson.dev.instagramclone.R

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation()
        Log.d(TAG, "onCreate: ${this.navNumber}")
    }

    override fun onStart() {
        super.onStart()
        setActivityChecked(2)
    }
}