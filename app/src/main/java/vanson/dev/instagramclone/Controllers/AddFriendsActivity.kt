package vanson.dev.instagramclone.Controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_add_friends.*
import kotlinx.android.synthetic.main.add_friends_item.view.*
import vanson.dev.instagramclone.*
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.Utilites.FirebaseHelper
import vanson.dev.instagramclone.Utilites.TaskSourceOnCompleteListener
import vanson.dev.instagramclone.Utilites.ValueEventListenerAdapter

class AddFriendsActivity : AppCompatActivity(), FriendsAdapter.Listener {
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
        val uid = mFirebase.auth.currentUser!!.uid
        add_friends_recycler.adapter = mAdapter
        add_friends_recycler.layoutManager = LinearLayoutManager(this)

        mFirebase.database.child("Users").addValueEventListener(ValueEventListenerAdapter {
            val allUsers = it.children.map { it.asUser()!! }
            val (userList, otherUserList) = allUsers.partition { it.uid == uid }
            mUser = userList.first()
            mUsers = otherUserList

            mAdapter.update(mUsers, mUser.follows)
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
        fun DatabaseReference.setValueTrueOrRemove(follow: Boolean) =
            if (follow) setValue(true) else removeValue()

        val followsTask =
            mFirebase.database.child("Users").child(mUser.uid).child("Follows").child(uid)
                .setValueTrueOrRemove(follow)
        val followersTask = mFirebase.database.child("Users").child(uid).child("Followers").child(
            mUser.uid
        )
            .setValueTrueOrRemove(follow)

        val feedPostsTask = task<Void> { taskSource ->
            mFirebase.database.child("Feed_Posts").child(uid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    val postsMap = if (follow) {
                        it.children.map { it.key to it.value }.toMap()
                    } else {
                        it.children.map { it.key to null }.toMap()
                    }
                    mFirebase.database.child("Feed_Posts").child(mUser.uid).updateChildren(postsMap)
                        .addOnCompleteListener { TaskSourceOnCompleteListener(taskSource) }
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

class FriendsAdapter(private val listener: Listener) :
    RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface Listener {
        fun follow(uid: String)
        fun unfollow(uid: String)
    }

    private var mPositions = mapOf<String, Int>()
    private var mFollows = mapOf<String, Boolean>()
    private var mUsers = listOf<User>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.add_friends_item, parent, false)
        )
    }

    override fun getItemCount(): Int = mUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val user = mUsers[position]
            view.photo_image.loadImage(user.photo)
            view.username_text.text = user.username
            view.name_text.text = user.name
            view.follow_btn.setOnClickListener { listener.follow(user.uid) }
            view.unfollow_btn.setOnClickListener { listener.unfollow(user.uid) }
            val follows = mFollows[user.uid] ?: false
            if (follows) {
                view.follow_btn.visibility = View.GONE
                view.unfollow_btn.visibility = View.VISIBLE
            } else {
                view.unfollow_btn.visibility = View.GONE
                view.follow_btn.visibility = View.VISIBLE
            }

        }
    }

    fun update(users: List<User>, follows: Map<String, Boolean>) {
        mUsers = users
        mPositions = users.withIndex().map { (idx, user) -> user.uid to idx }.toMap()
        mFollows = follows
        notifyDataSetChanged()
    }

    fun followed(uid: String) {
        mFollows += (uid to true)
        notifyItemChanged(mPositions[uid]!!)
    }

    fun unfollowed(uid: String) {
        mFollows -= uid
        notifyItemChanged(mPositions[uid]!!)
    }
}
