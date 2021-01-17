package vanson.dev.instagramclone.Controllers.addfriends

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.Utilites.FirebaseLiveData
import vanson.dev.instagramclone.Utilites.TaskSourceOnCompleteListener
import vanson.dev.instagramclone.Utilites.ValueEventListenerAdapter
import vanson.dev.instagramclone.asUser
import vanson.dev.instagramclone.mapCustom
import vanson.dev.instagramclone.task

interface AddFriendsRepository {
    fun getUsers(): LiveData<List<User>>
    fun currentUid(): String?
    fun addFollow(fromUid: String, toUid: String): Task<Unit>
    fun deleteFollow(fromUid: String, toUid: String): Task<Unit>
    fun addFollower(fromUid: String, toUid: String): Task<Unit>
    fun deleteFollower(fromUid: String, toUid: String): Task<Unit>
    fun copyFeedPosts(postsAuthorUid: String, uid: String): Task<Unit>
    fun deleteFeedPosts(postsAuthorUid: String, uid: String): Task<Unit>
}

class FirebaseAddFriendsRepository : AddFriendsRepository {
    private val reference = FirebaseDatabase.getInstance().reference
    override fun getUsers(): LiveData<List<User>> =
        FirebaseLiveData(reference.child("Users")).mapCustom { dataSnapshot ->
            dataSnapshot.children.map { it.asUser()!! }
        }


    override fun addFollow(fromUid: String, toUid: String): Task<Unit> =
        getFollowsRef(fromUid, toUid).setValue(true).toUnit()


    override fun deleteFollow(fromUid: String, toUid: String): Task<Unit> =
        getFollowsRef(fromUid, toUid).removeValue().toUnit()


    override fun addFollower(fromUid: String, toUid: String): Task<Unit> =
        getFollowersRef(fromUid, toUid).setValue(true).toUnit()

    override fun deleteFollower(fromUid: String, toUid: String): Task<Unit> =
        getFollowersRef(fromUid, toUid).removeValue().toUnit()


    override fun copyFeedPosts(postsAuthorUid: String, uid: String): Task<Unit> =
        task<Unit> { taskSource ->
            reference.child("Feed-Posts").child(postsAuthorUid)
                .orderByChild("uid")
                .equalTo(postsAuthorUid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    val postsMap = it.children.map { x -> x.key to x.value }.toMap()
                    reference.child("Feed-Posts").child(uid).updateChildren(postsMap)
                        .toUnit()
                        .addOnCompleteListener(TaskSourceOnCompleteListener(taskSource))
                })
        }

    override fun deleteFeedPosts(postsAuthorUid: String, uid: String): Task<Unit> =
        task<Unit> { taskSource ->
            reference.child("Feed-Posts").child(uid)
                .orderByChild("uid")
                .equalTo(postsAuthorUid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    val postsMap = it.children.map { x -> x.key to null }.toMap()
                    reference.child("Feed-Posts").child(uid).updateChildren(postsMap)
                        .toUnit()
                        .addOnCompleteListener(TaskSourceOnCompleteListener(taskSource))
                })
        }

    private fun getFollowsRef(fromUid: String, toUid: String) =
        reference.child("Users").child(fromUid).child("Follows").child(toUid)

    private fun getFollowersRef(fromUid: String, toUid: String) =
        reference.child("Users").child(toUid).child("Followers").child(fromUid)

    override fun currentUid(): String? = FirebaseAuth.getInstance().currentUser?.uid
}

fun Task<Void>.toUnit(): Task<Unit> = onSuccessTask { Tasks.forResult(Unit) }