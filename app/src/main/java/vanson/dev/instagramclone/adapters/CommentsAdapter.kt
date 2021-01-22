package vanson.dev.instagramclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comments_item.view.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.loadUserPhoto
import vanson.dev.instagramclone.controllers.common.setCaptionText
import vanson.dev.instagramclone.models.Comment
import vanson.dev.instagramclone.utilites.SimpleCallback

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    private var comments: List<Comment> = listOf()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.comments_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        with(holder.view) {
            photo.loadUserPhoto(comment.photo)
            text.setCaptionText(comment.username, comment.text, comment.timestampDate())
        }
    }

    override fun getItemCount(): Int = comments.size

    fun updateComments(newComments: List<Comment>) {
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(comments, newComments) { it.id })
        comments = newComments
        diffResult.dispatchUpdatesTo(this)
    }


}