package vanson.dev.instagramclone.Controllers.addfriends

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.mapCustom

class AddFriendsViewModel(private val repository: AddFriendsRepository) : ViewModel() {
    val currentUid = FirebaseAuth.getInstance().currentUser?.uid

    val userAndFriends: LiveData<Pair<User, List<User>>> =

        repository.getUsers().mapCustom { allUsers ->
            val (userList, otherUserList) = allUsers.partition { it.uid == currentUid }
            userList.first() to otherUserList
        }

    fun setFollow(currentUid: String, uid: String, follow: Boolean): Task<Void> {
        return if (follow) {
            Tasks.whenAll(
                repository.addFollow(currentUid, uid),
                repository.addFollower(currentUid, uid),
                repository.copyFeedPosts(postsAuthorUid = uid, uid = currentUid)
            )
        } else {
            Tasks.whenAll(
                repository.deleteFollow(currentUid, uid),
                repository.deleteFollower(currentUid, uid),
                repository.deleteFeedPosts(postsAuthorUid = uid, uid = currentUid)
            )
        }
    }
}