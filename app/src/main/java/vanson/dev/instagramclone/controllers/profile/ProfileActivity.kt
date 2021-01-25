package vanson.dev.instagramclone.controllers.profile

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_profile.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.adapters.ImagesAdapter
import vanson.dev.instagramclone.controllers.addfriends.AddFriendsActivity
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.common.loadUserPhoto
import vanson.dev.instagramclone.controllers.common.setupAuthGuard
import vanson.dev.instagramclone.controllers.editprofile.EditProfileActivity
import vanson.dev.instagramclone.controllers.profilesettings.ProfileSettingActivity
import vanson.dev.instagramclone.views.setupBottomNavigation

class ProfileActivity : BaseActivity() {
    private lateinit var mAdapter: ImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


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
        mAdapter = ImagesAdapter()
        images_recycler.adapter = mAdapter
        images_recycler.layoutManager = GridLayoutManager(this, 3)
        setupAuthGuard { uid ->
            setupBottomNavigation(uid,4)
            val mViewModel = initViewModel<ProfileViewModel>()
            mViewModel.init(uid)
            mViewModel.user.observe(this, Observer {
                it?.let {
                    profile_image.loadUserPhoto(it.photo)
                    username_text.text = it.username
                    followers_count_text.text = it.Followers.size.toString()
                    following_count_text.text = it.Follows.size.toString()

                }
            })

            mViewModel.images.observe(this, Observer {
                it?.let {
                    mAdapter.updateImages(it)//images
                    posts_count_text.text = it.size.toString()
                }
            })
        }
    }

    companion object {
        private val tag = "ProfileActivity"
    }
}

