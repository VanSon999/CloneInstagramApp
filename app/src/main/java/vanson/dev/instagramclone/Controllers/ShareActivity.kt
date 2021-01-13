package vanson.dev.instagramclone.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_share.*
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.Utilites.CameraHelper
import vanson.dev.instagramclone.Utilites.FirebaseHelper
import vanson.dev.instagramclone.Utilites.GlideApp
import vanson.dev.instagramclone.Utilites.ValueEventListenerAdapter
import vanson.dev.instagramclone.asUser
import vanson.dev.instagramclone.showToast
import java.util.*

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActivity"
    private lateinit var mCamera: CameraHelper
    private lateinit var mFirebase: FirebaseHelper
    private lateinit var mUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        Log.d(TAG, "onCreate: ${this.navNumber}")
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
        var uriImage = mCamera.mImageUri
        if (uriImage != null) {
            val uid = mFirebase.auth.currentUser!!.uid
            mFirebase.storage.child("Users").child(uid).child("images").child(
                uriImage.lastPathSegment!!
            ).putFile(uriImage).continueWithTask { task ->
                if (!task.isSuccessful) {
                    showToast(task.exception!!.message!!)
                }
                mFirebase.storage.child("Users").child(uid).child("images").child(
                    uriImage.lastPathSegment!!
                ).downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    val imageDownloadUrl = it.result.toString()
                    mFirebase.database.child("images").child(uid).push()
                        .setValue(it.result.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
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
                                showToast(it.exception!!.message!!)
                            }
                        }
                } else {
                    showToast(it.exception!!.message!!)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mCamera.REQUEST_CODE) {
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

data class FeedPost(
    val uid: String = "",
    val username: String = "",
    val image: String = "",
    val likeCount: Int = 0,
    val commentsCount: Int = 0,
    val caption: String = "",
    val comments: List<Comment> = emptyList(),
    val timestamp: Any = ServerValue.TIMESTAMP, //get value follow server
    val photo: String? = null
) {

    fun timestampDate(): Date = Date(timestamp as Long)
}

class Comment(val uid: String, val username: String, val text: String)