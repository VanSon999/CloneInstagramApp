package vanson.dev.instagramclone.controllers.addfriends

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import vanson.dev.instagramclone.controllers.common.BaseViewModel
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.repository.FeedPostsRepository
import vanson.dev.instagramclone.repository.UsersRepository
import vanson.dev.instagramclone.repository.common.mapCustom

class AddFriendsViewModel(
    onFailureListener: OnFailureListener,
    private val usersRepo: UsersRepository,
    private val feedPostsRepo: FeedPostsRepository
) : BaseViewModel(onFailureListener) {
    val userAndFriends: LiveData<Pair<User, List<User>>> =

        usersRepo.getUsers().mapCustom { allUsers ->
            val (userList, otherUserList) = allUsers.partition { it.uid == usersRepo.currentUid() }
            userList.first() to otherUserList
        }

    fun setFollow(currentUid: String, uid: String, follow: Boolean): Task<Void> {
        return (if (follow) {
            Tasks.whenAll(
                usersRepo.addFollow(currentUid, uid),
                usersRepo.addFollower(currentUid, uid),
                feedPostsRepo.copyFeedPosts(postsAuthorUid = uid, uid = currentUid)
            )
        } else {
            Tasks.whenAll(
                usersRepo.deleteFollow(currentUid, uid),
                usersRepo.deleteFollower(currentUid, uid),
                feedPostsRepo.deleteFeedPosts(postsAuthorUid = uid, uid = currentUid)
            )
        }).addOnFailureListener(onFailureListener)
    }
}