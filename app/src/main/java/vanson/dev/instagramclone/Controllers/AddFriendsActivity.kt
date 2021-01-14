package vanson.dev.instagramclone.Controllers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_add_friends.*
import vanson.dev.instagramclone.*
import vanson.dev.instagramclone.Adapters.FriendsAdapter
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.Utilites.FirebaseHelper
import vanson.dev.instagramclone.Utilites.TaskSourceOnCompleteListener
import vanson.dev.instagramclone.Utilites.ValueEventListenerAdapter

class AddFriendsActivity : AppCompatActivity(), FriendsAdapter.Listener {
    private val TAG = "AddFriendsActivity"
    private lateinit var mAdapter: FriendsAdapter
    private lateinit var mUsers: List<User>
    private lateinit var mUser: User
    private lateinit var mFirebase: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)
        mFirebase = FirebaseHelper(this)
        mAdapter = FriendsAdapter(this)
        back_image.setOnClickListener { finish() }
        val uid = mFirebase.currentUid()!!
        add_friends_recycler.adapter = mAdapter
        add_friends_recycler.layoutManager = LinearLayoutManager(this)

        mFirebase.database.child("Users").addValueEventListener(ValueEventListenerAdapter {
            val allUsers = it.children.map { it.asUser()!! }
            val (userList, otherUserList) = allUsers.partition { it.uid == uid }
            mUser = userList.first()
            mUsers = otherUserList

            mAdapter.update(mUsers, mUser.Follows)
        })
    }

    override fun follow(uid: String) {
        setFollow(uid, true) {
            mAdapter.followed(uid)
        }
    }

    override fun unfollow(uid: String) {
        setFollow(uid, false) {
            mAdapter.unfollowed(uid)
        }
    }

    private fun setFollow(uid: String, follow: Boolean, onSuccess: () -> Unit) {

        val followsTask =
            mFirebase.database.child("Users").child(mUser.uid).child("Follows").child(uid)
                .setValueTrueOrRemove(follow)
        val followersTask = mFirebase.database.child("Users").child(uid).child("Followers")
                .child(mUser.uid).setValueTrueOrRemove(follow)

        val feedPostsTask = task<Void> { taskSource ->
            mFirebase.database.child("Feed-Posts").child(uid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    val postsMap = if (follow) {
                        it.children.map { x -> x.key to x.value }.toMap()
                    } else {
                        it.children.map { x -> x.key to null }.toMap()
                    }
                    mFirebase.database.child("Feed-Posts").child(mUser.uid).updateChildren(postsMap)
                        .addOnCompleteListener(TaskSourceOnCompleteListener(taskSource))
                })
        }
        Tasks.whenAll(followsTask, followersTask, feedPostsTask).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                showToast(it.exception!!.message!!)
            }
        }
    }
}

