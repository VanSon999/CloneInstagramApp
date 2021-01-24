package vanson.dev.instagramclone.controllers.share

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_share.*
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.*
import vanson.dev.instagramclone.controllers.profile.ProfileActivity
import vanson.dev.instagramclone.repository.common.FirebaseHelper

class ShareActivity : BaseActivity() {
    private lateinit var mViewModel: ShareViewModel
    private lateinit var mCamera: CameraHelper
    private lateinit var mFirebase: FirebaseHelper
    private lateinit var mUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        setupAuthGuard {
            mViewModel = initViewModel()
            mFirebase = FirebaseHelper(this)
            mCamera = CameraHelper(this)
            mCamera.takeImageFromCamera()
            back_image.setOnClickListener { finish() }
            share_text.setOnClickListener { share() }

            mViewModel.user.observe(this, Observer {
                it?.let {
                    mUser = it
                }
            })

            mViewModel.gotoProfileActivity.observe(this , Observer {
                it.getContentIfNotHandled()?.let{
//                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                }
            })
        }
//        Log.d(tag, "onCreate: ${this.navNumber}")
    }

    private fun share() {
        mViewModel.share(mUser, mCamera.mImageUri, caption_input.text.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mCamera.requestCode) {
            if (resultCode == RESULT_OK) {
                post_image.loadImage(mCamera.mImageUri?.toString())
            } else {
                finish()
            }
        }
    }

    companion object {
        const val tag = "ShareActivity"
    }
}

