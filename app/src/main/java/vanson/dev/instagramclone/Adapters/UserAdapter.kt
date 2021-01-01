package vanson.dev.instagramclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R

class UserAdapter (private var mContext : Context, private var mUser: List<User>, private var isFragment: Boolean = false) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        var userName: TextView = itemView.findViewById(R.id.user_name_search)
        var userFullName: TextView = itemView.findViewById(R.id.user_full_name_search)
        var userProfileImage: CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
        var followButton: Button = itemView.findViewById(R.id.follow_btn_search)

        fun bindUser(user: User){
            userName.text = user.getUsername()
            userFullName.text = user.getFullName()
            Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(userProfileImage)
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        holder.bindUser(mUser[position])
    }

}