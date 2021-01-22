package vanson.dev.instagramclone.controllers

import android.os.Bundle
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.common.setupAuthGuard
import vanson.dev.instagramclone.views.setupBottomNavigation

class NotificationsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        setupBottomNavigation(3)
//        Log.d(TAG, "onCreate: ${this.navNumber}")
        setupAuthGuard {  }
    }

    companion object{
        private val tag = "LikesActivity"
    }
}