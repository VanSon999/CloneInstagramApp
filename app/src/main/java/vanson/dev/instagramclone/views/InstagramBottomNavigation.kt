package vanson.dev.instagramclone.views

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.nhaarman.supertooltips.ToolTip
import com.nhaarman.supertooltips.ToolTipRelativeLayout
import com.nhaarman.supertooltips.ToolTipView
import kotlinx.android.synthetic.main.bottom_navigation_view.*
import kotlinx.android.synthetic.main.notifications_tooltip_content.view.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.home.HomeActivity
import vanson.dev.instagramclone.controllers.notification.NotificationsActivity
import vanson.dev.instagramclone.controllers.notification.NotificationsViewModel
import vanson.dev.instagramclone.controllers.profile.ProfileActivity
import vanson.dev.instagramclone.controllers.search.SearchActivity
import vanson.dev.instagramclone.controllers.share.ShareActivity
import vanson.dev.instagramclone.models.Notification
import vanson.dev.instagramclone.models.NotificationType

@Suppress("DEPRECATION")
class InstagramBottomNavigation(
    private val uid: String,
    private val navNumber: Int,
    private val tooltipLayout: ToolTipRelativeLayout,
    private val bnv: BottomNavigationViewEx,
    private val activity: BaseActivity
) : LifecycleObserver {

    private var lastTooltipView: ToolTipView? = null
    private lateinit var mNotificationsContentView: View
    private lateinit var mViewModel: NotificationsViewModel

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        mViewModel = activity.initViewModel()
        mViewModel.init(uid)
        mViewModel.notifications.observe(activity, Observer {
            it?.let {
                showNotifications(it)
            }
        })
        mNotificationsContentView =
            activity.layoutInflater.inflate(R.layout.notifications_tooltip_content, null, false)
    }

    private fun showNotifications(notifications: List<Notification>) {
        if (lastTooltipView != null) { // why???
            val parent = mNotificationsContentView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(mNotificationsContentView) // why???
                lastTooltipView?.remove()
            }
            lastTooltipView = null
        }

        val newNotifications = notifications.filter { !it.read }
        val newNotificationsMap =
            newNotifications.groupBy { it.type }.mapValues { (_, values) -> values.size }

        fun setCount(image: ImageView, textView: TextView, type: NotificationType){
            val count = newNotificationsMap[type] ?: 0
            if(count == 0){
                image.visibility = View.GONE
                textView.visibility = View.GONE
            }else{
                image.visibility = View.VISIBLE
                textView.visibility = View.VISIBLE
                textView.text = count.toString()

            }
        }

        with(mNotificationsContentView){
            setCount(likes_image, likes_count_text, NotificationType.Like)
            setCount(follow_image, follow_count_text, NotificationType.Follow)
            setCount(comment_image, comment_count_text, NotificationType.Comment)
        }

        if(newNotifications.isNotEmpty()){
            val tooltip = ToolTip()
                .withColor(ContextCompat.getColor(activity, R.color.red))
                .withContentView(mNotificationsContentView)
                .withAnimationType(ToolTip.AnimationType.FROM_TOP)
                .withShadow()
            lastTooltipView = tooltipLayout.showToolTipForView(tooltip, bnv.getIconAt(NOTIFICATIONS_ICON_POS))
            lastTooltipView?.setOnToolTipViewClickedListener {
                mViewModel.setNotificationsRead(newNotifications)
                bnv.getBottomNavigationItemView(NOTIFICATIONS_ICON_POS).callOnClick()
            }
        }
    }

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

    companion object {
        const val tag = "BottomNavigation"
        const val NOTIFICATIONS_ICON_POS = 3
    }
}

fun BaseActivity.setupBottomNavigation(uid: String, navNumber: Int) {
    val bnv =
        InstagramBottomNavigation(uid, navNumber, tooltip_layout, bottom_navigation_view, this)
    this.lifecycle.addObserver(bnv)
}