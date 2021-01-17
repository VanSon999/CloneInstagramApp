package vanson.dev.instagramclone.Controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.acitivity_home.*
import vanson.dev.instagramclone.Adapters.FeedAdapter
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.Utilites.FirebaseHelper
import vanson.dev.instagramclone.Utilites.ValueEventListenerAdapter
import vanson.dev.instagramclone.asFeedPost
import vanson.dev.instagramclone.setValueTrueOrRemove

class HomeActivity : BaseActivity(0), FeedAdapter.Listener {
    private lateinit var mAdapter: FeedAdapter
    private val tag = "HomeActivity"
    private lateinit var mFirebase: FirebaseHelper
    private var mLikesListeners: Map<String, ValueEventListener> = emptyMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_home)
        setupBottomNavigation()
        Log.d(tag, "onCreate: ${this.navNumber}")

        mFirebase = FirebaseHelper(this)

        mFirebase.auth.addAuthStateListener {
            if (mFirebase.auth.currentUser == null) {
                startActivity(
                    Intent(
                        this,
                        LoginActivity::class.java
                    )
                )
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mFirebase.auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            mFirebase.database.child("Feed-Posts").child(currentUser.uid)
                .addValueEventListener(ValueEventListenerAdapter { dataSnapshot ->
                    val posts = dataSnapshot.children.map { it.asFeedPost()!! }
                        .sortedByDescending { it.timestampDate() }
//                    Log.d(TAG, "feedPosts: ${posts.first()?.timestampDate()}")
                    mAdapter = FeedAdapter(this, posts)
                    recycler_posts.adapter = mAdapter
                    recycler_posts.layoutManager = LinearLayoutManager(this)
                })
        }
    }

    override fun toggleLike(postId: String) {
        val reference =
            mFirebase.database.child("likes").child(postId).child(mFirebase.currentUid()!!)
        reference.addListenerForSingleValueEvent(ValueEventListenerAdapter {
            reference.setValueTrueOrRemove(!it.exists())
        })
    }

    override fun loadLikes(id: String, position: Int) {
        fun createListener() =
            mFirebase.database.child("likes").child(id)
                .addValueEventListener(ValueEventListenerAdapter { snapshot ->
                    val userLikes = snapshot.children.map { it.key }.toSet() //Set: O(1), List: O(n)
                    val postLikes =
                        FeedPostLikes(userLikes.size, userLikes.contains(mFirebase.currentUid()))
                    mAdapter.updatePostLikes(position, postLikes)
                })

        if (mLikesListeners[id] == null) {
            mLikesListeners = mLikesListeners + (id to createListener())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mLikesListeners.values.forEach{mFirebase.database.removeEventListener(it)}
    }
}

data class FeedPostLikes(val likesCount: Int, val likeByUser: Boolean)

