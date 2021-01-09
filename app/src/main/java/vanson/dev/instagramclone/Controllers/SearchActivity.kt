package vanson.dev.instagramclone.Controllers

import android.os.Bundle
import android.util.Log
import vanson.dev.instagramclone.R

class SearchActivity : BaseActivity(1) {
    private val TAG = "SearchActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation()
        Log.d(TAG, "onCreate: ${this.navNumber}")
    }

    override fun onStart() {
        super.onStart()
        setActivityChecked(1)
    }
}
