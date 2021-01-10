package vanson.dev.instagramclone.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_share.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.Utilites.CameraHelper
import vanson.dev.instagramclone.Utilites.GlideApp

class ShareActivity : BaseActivity(2) {
    private val TAG = "ShareActivity"
    private lateinit var mCameraHelper : CameraHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        setupBottomNavigation()
        Log.d(TAG, "onCreate: ${this.navNumber}")

        mCameraHelper = CameraHelper(this)
        mCameraHelper.takeImageFromCamera()

        back_image.setOnClickListener { finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == mCameraHelper.REQUEST_CODE && resultCode == RESULT_OK){
            GlideApp.with(this).load(mCameraHelper.mImageUri).centerCrop().into(post_image)
        }
    }
    override fun onStart() {
        super.onStart()
        setActivityChecked(2)
    }
}