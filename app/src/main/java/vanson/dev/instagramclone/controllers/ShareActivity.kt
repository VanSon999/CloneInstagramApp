package vanson.dev.instagramclone.controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_share.*
import vanson.dev.instagramclone.models.FeedPost
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.utilites.CameraHelper
import vanson.dev.instagramclone.utilites.FirebaseHelper
import vanson.dev.instagramclone.utilites.GlideApp
import vanson.dev.instagramclone.utilites.ValueEventListenerAdapter
import vanson.dev.instagramclone.asUser
import vanson.dev.instagramclone.showToast

class ShareActivity : BaseActivity() {
    private val tag = "ShareActivity"
    private lateinit var mCamera: CameraHelper
    private lateinit var mFirebase: FirebaseHelper
    private lateinit var mUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

//        Log.d(tag, "onCreate: ${this.navNumber}")
        mFirebase = FirebaseHelper(this)
        mCamera = CameraHelper(this)
        mCamera.takeImageFromCamera()
        back_image.setOnClickListener { finish() }
        share_text.setOnClickListener { share() }
        mFirebase.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.asUser()!!
        })
    }

    private fun share() {
        val uriImage = mCamera.mImageUri
        if (uriImage != null) {
            val uid = mFirebase.currentUid()!!
            mFirebase.storage.child("Users").child(uid).child("images").child(
                uriImage.lastPathSegment!!
            ).putFile(uriImage).continueWithTask { task ->
                if (!task.isSuccessful) {
                    showToast(task.exception!!.message!!)
                }
                mFirebase.storage.child("Users").child(uid).child("images").child(
                    uriImage.lastPathSegment!!
                ).downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val imageDownloadUrl = task.result.toString()
                    mFirebase.database.child("images").child(uid).push()
                        .setValue(task.result.toString())
                        .addOnCompleteListener { task1 ->
                            if (task1.isSuccessful) {
                                mFirebase.database.child("Feed-Posts").child(uid).push()
                                    .setValue(mkFeedPost(uid, imageDownloadUrl))
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            startActivity(Intent(this, ProfileActivity::class.java))
                                            finish()
                                        } else {
                                            showToast(it.exception!!.message!!)
                                        }
                                    }
                            } else {
                                showToast(task1.exception!!.message!!)
                            }
                        }
                } else {
                    showToast(task.exception!!.message!!)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mCamera.requestCode) {
            if (resultCode == RESULT_OK) {
                GlideApp.with(this).load(mCamera.mImageUri).centerCrop().into(post_image)
            } else {
                finish()
            }
        }
    }

    private fun mkFeedPost(uid: String, imageDownloadUrl: String): FeedPost {
        return FeedPost(
            uid = uid,
            username = mUser.username,
            image = imageDownloadUrl,
            caption = caption_input.text.toString(),
            photo = mUser.photo
        )
    }
}

