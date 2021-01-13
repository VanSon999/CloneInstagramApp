package vanson.dev.instagramclone.Controllers

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.acitivity_home.*
import kotlinx.android.synthetic.main.feed_item.view.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.Utilites.FirebaseHelper
import vanson.dev.instagramclone.Utilites.GlideApp
import vanson.dev.instagramclone.Utilites.ValueEventListenerAdapter
import vanson.dev.instagramclone.loadImage
import vanson.dev.instagramclone.showToast

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
                    val posts = it.children.map { it.getValue(FeedPost::class.java)!! }
//                    Log.d(TAG, "feedPosts: ${posts.first()?.timestampDate()}")
                    recycler_posts.adapter = FeedAdapter(posts)
                    recycler_posts.layoutManager = LinearLayoutManager(this)
                })
        }
    }
}

class FeedAdapter(private val posts: List<FeedPost>) :
    RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        with(holder) {
            view.user_photo_image.loadImage(post.photo)
            view.username_text.text = post.username
            view.post_image.loadImage(post.image)
            if (post.likeCount == 0) {
                view.likes_text.visibility = View.GONE
            } else {
                view.likes_text.visibility = View.VISIBLE
                view.likes_text.text = "${post.likeCount} likes"
            }
            //Spannable: username(bold, clickable) caption
            view.caption_text.setCaptionText(post.username, post.caption)
        }
    }

    private fun TextView.setCaptionText(username: String, caption: String) {
        val usernameSpannable = SpannableString(username)
        usernameSpannable.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            usernameSpannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        usernameSpannable.setSpan(object : ClickableSpan() {
            override fun onClick(p0: View) {
                p0.context.showToast("Username is clicked")
            }

            override fun updateDrawState(ds: TextPaint) {}
        }, 0, usernameSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        text = SpannableStringBuilder().append(usernameSpannable).append(" ").append(caption)
        movementMethod = LinkMovementMethod.getInstance() // support for ClickableSpan()
    }

//    private fun ImageView.loadImage(urlImage: String?) {
//        GlideApp.with(this).load(urlImage).centerCrop().into(this)
//    }
}