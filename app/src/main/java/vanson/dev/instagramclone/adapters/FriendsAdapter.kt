package vanson.dev.instagramclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_friends_item.view.*
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.loadUserPhoto

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
            view.photo_image.loadUserPhoto(user.photo)
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
        mFollows = mFollows + (uid to true)
        notifyItemChanged(mPositions[uid] ?: error(""))
    }

    fun unfollowed(uid: String) {
        mFollows = mFollows - uid
        notifyItemChanged(mPositions[uid] ?: error(""))
    }
}