package vanson.dev.instagramclone.Controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.acitivity_home.*
import vanson.dev.instagramclone.Adapters.FeedAdapter
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.Utilites.FirebaseHelper
import vanson.dev.instagramclone.Utilites.ValueEventListenerAdapter

class HomeActivity : BaseActivity(0) {
    private val TAG = "HomeActivity"
    private lateinit var mFirebase: FirebaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_home)
        setupBottomNavigation()
        Log.d(TAG, "onCreate: ${this.navNumber}")

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
                .addValueEventListener(ValueEventListenerAdapter {
                    val posts = it.children.map { it.getValue(FeedPost::class.java)!! }.sortedByDescending { it.timestampDate() }
//                    Log.d(TAG, "feedPosts: ${posts.first()?.timestampDate()}")
                    recycler_posts.adapter = FeedAdapter(posts)
                    recycler_posts.layoutManager = LinearLayoutManager(this)
                })
        }
    }
}

