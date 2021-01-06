package vanson.dev.instagramclone.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import vanson.dev.instagramclone.Models.Post
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R

class PostAdapter(
    private val mPost: List<Post>,
    private val mContext: Context
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

//    private var firebaseUser: FirebaseUser? =null
    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profileImage: CircleImageView = itemView.findViewById(R.id.user_profile_image_home)
        var postImage: ImageView = itemView.findViewById(R.id.post_image_home)
        var likeButton: ImageView = itemView.findViewById(R.id.post_image_like_btn)
        var commentButton: ImageView = itemView.findViewById(R.id.post_image_comment_btn)
        var saveButton: ImageView = itemView.findViewById(R.id.post_save_comment_btn)
        var userName: TextView = itemView.findViewById(R.id.user_name_home)
        var likes: TextView = itemView.findViewById(R.id.likes)
        var publisher: TextView = itemView.findViewById(R.id.publisher)
        var description: TextView = itemView.findViewById(R.id.description)
        var comments: TextView = itemView.findViewById(R.id.comments)

        var userRef: Query? = null
        var userRefListener: ValueEventListener? = null

        fun pusblisherInfo(publisherId: String) {
            if(userRef != null && userRefListener != null){
                userRef!!.removeEventListener(userRefListener!!)
            }
            userRef = FirebaseDatabase.getInstance().reference.child("Users").child(publisherId)

            userRefListener = object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val user = snapshot.getValue<User>(User::class.java)
                        Log.d("PostAdapter_2", user!!.getFullName())
                        Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profileImage)
                        userName.text = user!!.getUsername()
                        publisher.text = user!!.getFullName()
                    }
                }
            }
            userRef!!.addValueEventListener(userRefListener!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.posts_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        firebaseUser = FirebaseAuth.getInstance().currentUser
        val post = mPost[position]
        Log.d("PostAdapter_1", post.getImage())
        Picasso.get().load(post.getImage()).into(holder.postImage)

        holder.pusblisherInfo(post.getPublisher())
    }
}