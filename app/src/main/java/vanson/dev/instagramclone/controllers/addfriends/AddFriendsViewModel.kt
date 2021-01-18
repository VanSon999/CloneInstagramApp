package vanson.dev.instagramclone.controllers.addfriends

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import vanson.dev.instagramclone.mapCustom
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.repository.FeedPostsRepository
import vanson.dev.instagramclone.repository.UsersRepository

class AddFriendsViewModel(private val usersRepo: UsersRepository, private val feedPostsRepo: FeedPostsRepository) : ViewModel() {
    val userAndFriends: LiveData<Pair<User, List<User>>> =

        usersRepo.getUsers().mapCustom { allUsers ->
            val (userList, otherUserList) = allUsers.partition { it.uid == usersRepo.currentUid() }
            userList.first() to otherUserList
        }

    fun setFollow(currentUid: String, uid: String, follow: Boolean): Task<Void> {
        return if (follow) {
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
        }
    }
}