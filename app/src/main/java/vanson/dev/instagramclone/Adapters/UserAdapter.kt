package vanson.dev.instagramclone.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R

class UserAdapter (private var mContext : Context, private var mUser: List<User>, private var isFragment: Boolean = false) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var firebaseUser: FirebaseUser? = null
    class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView){
        var userName: TextView = itemView.findViewById(R.id.user_name_search)
        var userFullName: TextView = itemView.findViewById(R.id.user_full_name_search)
        var userProfileImage: CircleImageView = itemView.findViewById(R.id.user_profile_image_search)
        var followButton: Button = itemView.findViewById(R.id.follow_btn_search)

        fun bindUser(user: User, firebase: FirebaseUser?){
            userName.text = user.getUsername()
            userFullName.text = user.getFullName()
            Picasso.get().load(user.getImage()).placeholder(R.drawable.profile).into(userProfileImage)
            //Problem!!!
            checkFollowingStatus(user.getUid(), followButton, firebase)
            //................
            followButton.setOnClickListener {
                if(followButton.text.toString() == "Follow"){
                    if (firebase != null) {
                        FirebaseDatabase.getInstance().reference.child("Follow")
                                .child(firebase.uid.toString())
                                .child("Following")
                                .child(user.getUid())
                                .setValue(true).addOnCompleteListener { task ->
                                    if(task.isSuccessful){
                                        FirebaseDatabase.getInstance().reference.child("Follow")
                                                .child(user.getUid())
                                                .child("Followers")
                                                .child(firebase.uid.toString())
                                                .setValue(true).addOnCompleteListener { task ->
                                                    if(task.isSuccessful){

                                                    }
                                                }
                                    }
                                }
                    }
                }else{
                    if (firebase != null) {
                        FirebaseDatabase.getInstance().reference.child("Follow")
                                .child(firebase.uid.toString())
                                .child("Following")
                                .child(user.getUid())
                                .removeValue().addOnCompleteListener { task ->
                                    if(task.isSuccessful){
                                        FirebaseDatabase.getInstance().reference.child("Follow")
                                                .child(user.getUid())
                                                .child("Followers")
                                                .child(firebase.uid.toString())
                                                .removeValue().addOnCompleteListener { task ->
                                                    if(task.isSuccessful){

                                                    }
                                                }
                                    }
                                }
                    }
                }
            }
        }

        private fun checkFollowingStatus(uid: String, followButton: Button, firebase: FirebaseUser?) {
            val followingRef = firebase?.uid.let { it ->
                FirebaseDatabase.getInstance().reference.child("Follow")
                        .child(it.toString())
                        .child("Following")
            }

            followingRef.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.child(uid).exists()){
                        followButton.text = "Following"
                    }else{
                        followButton.text = "Follow"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        holder.bindUser(mUser[position], firebaseUser)
    }

}