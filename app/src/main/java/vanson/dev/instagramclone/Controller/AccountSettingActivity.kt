package vanson.dev.instagramclone.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_account_setting.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R

class AccountSettingActivity : AppCompatActivity() {
    lateinit var fireBaseUser: FirebaseUser
    private var changeImage: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)

        fireBaseUser = FirebaseAuth.getInstance().currentUser!!
        userInfo()
        logout_btn_profile_frag.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        save_info_profile_btn.setOnClickListener {
            if(changeImage){

            }else{
                saveOnlyInfoUser()
            }
        }
    }

    private fun saveOnlyInfoUser() {
        when {
            full_name_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "You need fill Full Name!", Toast.LENGTH_LONG).show()
            }
            username_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "You need fill User Name!", Toast.LENGTH_LONG).show()
            }
            bio_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "You need fill Bio!", Toast.LENGTH_LONG).show()
            }
            else -> {
                val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(fireBaseUser.uid)
                val userMap = HashMap<String, Any>()
                userMap["fullname"] = full_name_profile_frag.text.toString()
                userMap["search_name"] = full_name_profile_frag.text.toString().toLowerCase()
                userMap["username"] = username_profile_frag.text.toString()
                userMap["bio"] = bio_profile_frag.text.toString()
                usersRef.updateChildren(userMap)

                Toast.makeText(this, "Your profile has been update successfully.", Toast.LENGTH_LONG).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun userInfo(){
        val userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fireBaseUser.uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
//                if(context != null){
//                    return
//                }

                if(snapshot.exists()){
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profile_image_view_profile_frag)
                    full_name_profile_frag.setText(user.getFullName())
                    username_profile_frag.setText(user.getUsername())
                    bio_profile_frag.setText(user.getBio())
                }
            }
        })
    }
}