package vanson.dev.instagramclone.controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_profile.*
import vanson.dev.instagramclone.adapters.ImagesAdapter
import vanson.dev.instagramclone.controllers.addfriends.AddFriendsActivity
import vanson.dev.instagramclone.controllers.editprofile.EditProfileActivity
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.utilites.FirebaseHelper
import vanson.dev.instagramclone.utilites.ValueEventListenerAdapter
import vanson.dev.instagramclone.asUser
import vanson.dev.instagramclone.loadUserPhoto
import vanson.dev.instagramclone.views.setupBottomNavigation

class ProfileActivity : BaseActivity() {
    private lateinit var mUser: User
    private lateinit var mFirebase: FirebaseHelper
    private val tag = "ProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation(4)
//        Log.d(tag, "onCreate: ${this.navNumber}")

        edit_profile_btn.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        add_friends_image.setOnClickListener {
            val intent = Intent(this, AddFriendsActivity::class.java)
            startActivity(intent)
        }
        settings_image.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }
        mFirebase = FirebaseHelper(this)
        mFirebase.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.asUser()!!
            profile_image.loadUserPhoto(mUser.photo)
            username_text.text = mUser.username
        })

        images_recycler.layoutManager = GridLayoutManager(this, 3)
        mFirebase.database.child("images").child(mFirebase.currentUid()!!)
            .addValueEventListener(ValueEventListenerAdapter { dataSnapshot ->
                val images = dataSnapshot.children.map { it.getValue(String::class.java)!! }
                images_recycler.adapter = ImagesAdapter(images)
            })
    }
}

