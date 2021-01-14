package vanson.dev.instagramclone.Controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_profile.*
import vanson.dev.instagramclone.Adapters.ImagesAdapter
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.Utilites.FirebaseHelper
import vanson.dev.instagramclone.Utilites.ValueEventListenerAdapter
import vanson.dev.instagramclone.asUser
import vanson.dev.instagramclone.loadUserPhoto

class ProfileActivity : BaseActivity(4) {
    private lateinit var mUser: User
    private lateinit var mFirebase: FirebaseHelper
    private val TAG = "ProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation()
        Log.d(TAG, "onCreate: ${this.navNumber}")

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
        mFirebase.database.child("images").child(mFirebase.currentUid()!!).addValueEventListener(ValueEventListenerAdapter{
            val images = it.children.map {it.getValue(String:: class.java)!!}
            images_recycler.adapter = ImagesAdapter(images)
        })
    }
}

