package vanson.dev.instagramclone.controllers

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Tasks
import kotlinx.android.synthetic.main.activity_share.*
import vanson.dev.instagramclone.controllers.common.showToast
import vanson.dev.instagramclone.controllers.profile.ProfileActivity
import vanson.dev.instagramclone.models.FeedPost
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.repository.UsersRepository

class ShareViewModel(private val usersRepo: UsersRepository, private val onFailureListener: OnFailureListener) : ViewModel() {
    val user = usersRepo.getUser()

    fun share(user: User, uriImage: Uri?, caption: String) {
        if (uriImage != null) {
            usersRepo.uploadUserImage(user.uid, uriImage).onSuccessTask { downloadUrl ->
                Tasks.whenAll(
                    usersRepo.setUserImage(user.uid, downloadUrl!!),
                    usersRepo.createFeedPost(user.uid, mkFeedPost(user, caption, downloadUrl.toString()))
                )
            }.addOnFailureListener(onFailureListener) // it is listener for both in and out uploadUserImage task
        }
    }

    private fun mkFeedPost(user: User, caption: String, imageDownloadUrl: String): FeedPost {
        return FeedPost(
            uid = user.uid,
            username = user.username,
            image = imageDownloadUrl,
            caption = caption,
            photo = user.photo
        )
    }
}
