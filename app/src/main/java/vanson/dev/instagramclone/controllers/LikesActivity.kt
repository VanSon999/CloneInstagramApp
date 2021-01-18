package vanson.dev.instagramclone.controllers

import android.os.Bundle
import android.util.Log
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.views.setupBottomNavigation

class LikesActivity : BaseActivity() {
    private val TAG = "LikesActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation(3)
//        Log.d(TAG, "onCreate: ${this.navNumber}")
    }
}