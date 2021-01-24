package vanson.dev.instagramclone.views

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import kotlinx.android.synthetic.main.bottom_navigation_view.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.*
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.home.HomeActivity
import vanson.dev.instagramclone.controllers.notification.NotificationsActivity
import vanson.dev.instagramclone.controllers.profile.ProfileActivity
import vanson.dev.instagramclone.controllers.share.ShareActivity

@Suppress("DEPRECATION")
class InstagramBottomNavigation(
    private val navNumber: Int,
    private val bnv: BottomNavigationViewEx,
    activity: AppCompatActivity
) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Log.d(tag, "onResume: $navNumber")
        bnv.menu.getItem(navNumber).isChecked = true
    }

    init {
        bnv.setTextVisibility(false)
        bnv.enableItemShiftingMode(false)
        bnv.enableShiftingMode(false)
        bnv.enableAnimation(false)
        for (x in 0 until bnv.menu.size()) {
            bnv.setIconTintList(x, null)
        }

        bnv.setOnNavigationItemSelectedListener {
            val nextActivity = when (it.itemId) {
                R.id.nav_ic_home -> HomeActivity::class.java
                R.id.nav_ic_likes -> NotificationsActivity::class.java
                R.id.nav_ic_search -> SearchActivity::class.java
                R.id.nav_ic_share -> ShareActivity::class.java
                R.id.nav_ic_profile -> ProfileActivity::class.java
                else -> {
                    null
                }
            }
            if (nextActivity != null) {
                val intent = Intent(activity, nextActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                activity.overridePendingTransition(0, 0) //turn of animation convert Activity
                activity.startActivity(intent)
                true
            } else {
                false
            }
        }
    }

    companion object{
        const val tag = "BottomNavigation"
    }
}

fun BaseActivity.setupBottomNavigation(navNumber: Int) {
    val bnv = InstagramBottomNavigation(navNumber, bottom_navigation_view, this)
    this.lifecycle.addObserver(bnv)
}