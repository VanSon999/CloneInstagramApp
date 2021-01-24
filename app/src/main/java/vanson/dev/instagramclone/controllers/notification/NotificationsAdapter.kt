package vanson.dev.instagramclone.controllers.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.notification_item.view.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.loadImage
import vanson.dev.instagramclone.controllers.common.loadImageOrHide
import vanson.dev.instagramclone.controllers.common.loadUserPhoto
import vanson.dev.instagramclone.controllers.common.setCaptionText
import vanson.dev.instagramclone.models.Notification
import vanson.dev.instagramclone.models.NotificationType
import vanson.dev.instagramclone.utilites.SimpleCallback

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    private var notifications = listOf<Notification>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]
        with(holder.itemView) {
            user_photo.loadUserPhoto(notification.photo)
            val notificationText =
                when (notification.type) {
                    NotificationType.Comment -> context.getString(
                        R.string.commented,
                        notification.commentText
                    )
                    NotificationType.Like -> context.getString(R.string.liked_your_post)
                    NotificationType.Follow -> context.getString(R.string.started_following_your)
                }
            text.setCaptionText(notification.username, notificationText, notification.timestampDate())
            post_image.loadImageOrHide(notification.postImage)
        }
    }

    override fun getItemCount(): Int = notifications.size

    fun updateNotifications(newNotifications: List<Notification>) {
        val diffResult =
            DiffUtil.calculateDiff(SimpleCallback(notifications, newNotifications) { it.id })
        this.notifications = newNotifications
        diffResult.dispatchUpdatesTo(this)
    }
}