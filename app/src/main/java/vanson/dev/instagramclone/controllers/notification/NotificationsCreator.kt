package vanson.dev.instagramclone.controllers.notification

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import vanson.dev.instagramclone.models.FeedPost
import vanson.dev.instagramclone.models.Notification
import vanson.dev.instagramclone.models.NotificationType
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.repository.FeedPostsRepository
import vanson.dev.instagramclone.repository.NotificationsRepository
import vanson.dev.instagramclone.repository.UsersRepository
import vanson.dev.instagramclone.repository.common.observeFirstNotNull
import vanson.dev.instagramclone.repository.common.zip
import vanson.dev.instagramclone.utilites.BaseEventListener
import vanson.dev.instagramclone.utilites.Event
import vanson.dev.instagramclone.utilites.EventBus

class NotificationsCreator(
    private val notificationsRepo: NotificationsRepository,
    private val usersRepo: UsersRepository,
    private val feedPostsRepo: FeedPostsRepository
) : BaseEventListener() {


    init {
        EventBus.events.observe(this, Observer { event_ ->
            event_?.let { event ->
                when (event) {
                    is Event.CreateFollow -> {
                        usersRepo.getUser(event.fromUid).observeFirstNotNull(this) { user ->
                            val notification = Notification(
                                uid = user.uid,
                                username = user.username,
                                photo = user.photo,
                                type = NotificationType.Follow
                            )
                            notificationsRepo.createNotifications(event.toUid, notification)
                                .addOnFailureListener {
                                    Log.d(TAG, "Failed to create notification", it)
                                }
                        }
                    }
                    is Event.CreateComment -> {
                        feedPostsRepo.getFeedPost(uid = event.comment.uid, postId = event.postId)
                            .observeFirstNotNull(this) { post ->
                                val notification = Notification(
                                    uid = event.comment.uid,
                                    username = event.comment.username,
                                    photo = event.comment.photo,
                                    type = NotificationType.Comment,
                                    postId = event.postId,
                                    commentText = event.comment.text,
                                    postImage = post.image
                                )

                                notificationsRepo.createNotifications(post.uid, notification)
                                    .addOnFailureListener {
                                        Log.d(TAG, "Failed to create notification", it)
                                    }
                            }
                    }
                    is Event.CreateLike -> {
                        val userData = usersRepo.getUser(event.uid)
                        val postData =
                            feedPostsRepo.getFeedPost(uid = event.uid, postId = event.postId)

                        userData.zip(postData).observeFirstNotNull(this) { (user, post) ->

                            val notification = Notification(
                                uid = user.uid,
                                username = user.username,
                                photo = user.photo,
                                type = NotificationType.Like,
                                postId = post.id,
                                postImage = post.image
                            )
                            notificationsRepo.createNotifications(post.uid, notification)
                                .addOnFailureListener {
                                    Log.d(TAG, "Failed to create notification", it)
                                }
                        }
                    }
                    is Event.CreateSearchPost -> {
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "NotificationsCreator"
    }
}