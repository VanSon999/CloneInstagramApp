package vanson.dev.instagramclone.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_share.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.Utilites.CameraHelper
import vanson.dev.instagramclone.Utilites.FirebaseHelper
import vanson.dev.instagramclone.Utilites.GlideApp
import vanson.dev.instagramclone.showToast

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActivity"
    private lateinit var mCamera : CameraHelper
    private lateinit var mFirebase : FirebaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        Log.d(TAG, "onCreate: ${this.navNumber}")
        mFirebase = FirebaseHelper(this)
        mCamera = CameraHelper(this)
        mCamera.takeImageFromCamera()
        back_image.setOnClickListener { finish() }
        share_text.setOnClickListener { share() }
    }

    private fun share() {
        var uriImage = mCamera.mImageUri
        if(uriImage != null)
        {
            val uid = mFirebase.auth.currentUser!!.uid
            mFirebase.storage.child("Users").child(uid).child("images").child(
                uriImage.lastPathSegment!!
            ).putFile(uriImage).continueWithTask{ task ->
                if (!task.isSuccessful) {
                    showToast(task.exception!!.message!!)
                }
                mFirebase.storage.child("Users").child(uid).child("images").child(
                    uriImage.lastPathSegment!!
                ).downloadUrl
            }.addOnCompleteListener{
                if (it.isSuccessful){
                    mFirebase.database.child("images").child(uid).push().setValue(it.result.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                startActivity(Intent(this, ProfileActivity::class.java))
                                finish()
                            }else{
                                showToast(it.exception!!.message!!)
                            }
                        }
                }else{
                    showToast(it.exception!!.message!!)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == mCamera.REQUEST_CODE){
            if(resultCode == RESULT_OK){
                GlideApp.with(this).load(mCamera.mImageUri).centerCrop().into(post_image)
            }else{
                finish()
            }
        }
    }
}