package vanson.dev.instagramclone.controllers

import android.os.Bundle
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.common.setupAuthGuard
import vanson.dev.instagramclone.views.setupBottomNavigation

class SearchActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation(1)
        setupAuthGuard {  }
    }
    companion object{
        const val tag = "SearchActivity"
    }
}
