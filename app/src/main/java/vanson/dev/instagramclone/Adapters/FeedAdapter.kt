package vanson.dev.instagramclone.Adapters

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.feed_item.view.*
import vanson.dev.instagramclone.Controllers.FeedPost
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.loadImage
import vanson.dev.instagramclone.loadUserPhoto
import vanson.dev.instagramclone.showToast

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
            view.user_photo_image.loadUserPhoto(post.photo)
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