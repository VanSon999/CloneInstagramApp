package vanson.dev.instagramclone.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.ServerValue
import java.util.*

data class Notification(
    val uid: String = "",
    val username: String = "",
    val photo: String? = null,
    val postId: String? = null,
    val postImage: String? = null,
    val commentText: String? = null,
    val type: NotificationType = NotificationType.Like,
    val read: Boolean = false,
    val timestamp: Any = ServerValue.TIMESTAMP,
    @get:Exclude val id: String = ""
) {

    fun timestampDate(): Date = Date(timestamp as Long)
}

enum class NotificationType{
    Follow, Like, Comment
}