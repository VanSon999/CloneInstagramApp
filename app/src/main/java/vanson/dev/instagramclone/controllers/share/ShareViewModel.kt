package vanson.dev.instagramclone.controllers.share

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Tasks
import vanson.dev.instagramclone.controllers.common.BaseViewModel
import vanson.dev.instagramclone.models.FeedPost
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.repository.FeedPostsRepository
import vanson.dev.instagramclone.repository.UsersRepository
import vanson.dev.instagramclone.utilites.EventWrapper

class ShareViewModel(
    private val usersRepo: UsersRepository,
    private val feedPostRepo: FeedPostsRepository,
    onFailureListener: OnFailureListener
) : BaseViewModel(onFailureListener) {

    private val _gotoProfileActivity = MutableLiveData<EventWrapper<Unit>>()
    val gotoProfileActivity = _gotoProfileActivity

    val user = usersRepo.getUser()

    fun share(user: User, uriImage: Uri?, caption: String) {
        if (uriImage != null) {
            usersRepo.uploadUserImage(user.uid, uriImage).onSuccessTask { downloadUrl ->
                Tasks.whenAll(
                    usersRepo.setUserImage(user.uid, downloadUrl!!),
                    feedPostRepo.createFeedPost(
                        user.uid,
                        mkFeedPost(user, caption, downloadUrl.toString())
                    )
                )
            }.addOnCompleteListener {
                _gotoProfileActivity.value = EventWrapper(Unit)
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
