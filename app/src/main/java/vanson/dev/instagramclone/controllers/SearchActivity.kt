package vanson.dev.instagramclone.controllers

import android.os.Bundle
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.views.setupBottomNavigation

class SearchActivity : BaseActivity() {
    private val TAG = "SearchActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation(1)
//        Log.d(TAG, "onCreate: ${this.navNumber}")
    }
}
