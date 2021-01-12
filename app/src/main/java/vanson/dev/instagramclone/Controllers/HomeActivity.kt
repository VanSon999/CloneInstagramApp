package vanson.dev.instagramclone.Controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.acitivity_home.*
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

        sign_out_text.setOnClickListener {
            mFirebase.auth.signOut()
        }
        mFirebase.auth.addAuthStateListener {
            if(mFirebase.auth.currentUser == null){
                startActivity(Intent(this,
                    LoginActivity::class.java))
                finish()
            }
        }
        mFirebase.database.child("Feed-Posts").child(mFirebase.auth.currentUser!!.uid).addValueEventListener(ValueEventListenerAdapter{
            val posts = it.children.map { it.getValue(FeedPost::class.java)}
            Log.d(TAG, "feedPosts: ${posts.first()?.timestampDate()}")
        })
    }

    override fun onStart() {
        super.onStart()
        if(mFirebase.auth.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}