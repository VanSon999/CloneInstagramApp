package vanson.dev.instagramclone.controllers.notification

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_notifications.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.common.setupAuthGuard
import vanson.dev.instagramclone.views.setupBottomNavigation

class NotificationsActivity : BaseActivity() {
    private lateinit var mAdapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        setupBottomNavigation(3)
//        Log.d(TAG, "onCreate: ${this.navNumber}")
        setupAuthGuard {uid ->
            mAdapter = NotificationsAdapter()
            notification_recycler.layoutManager = LinearLayoutManager(this)
            notification_recycler.adapter = mAdapter

            val viewModel = initViewModel<NotificationsViewModel>()
            viewModel.init(uid)
            viewModel.notifications.observe(this, Observer {
                it?.let{
                    mAdapter.updateNotifications(it)
                    viewModel.setNotificationsRead(it)
                }
            })
        }
    }

    companion object{
        private val tag = "LikesActivity"
    }
}