package vanson.dev.instagramclone.controllers.editprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_edit_profile.*
import vanson.dev.instagramclone.*
import vanson.dev.instagramclone.controllers.common.*
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.views.PasswordDialog

class EditProfileActivity : BaseActivity(), PasswordDialog.Listener {

    private lateinit var mViewModel: EditProfileViewModel
    private lateinit var mPendingUser: User
    private lateinit var mUser: User
    private lateinit var mCamera: CameraHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(tag, "onCreate")

        setupAuthGuard {
            mCamera = CameraHelper(this)
            mViewModel = initViewModel()
            close_image.setOnClickListener {
                finish()
            }
            save_image.setOnClickListener { updateProfile() }
            change_avatar_text.setOnClickListener { mCamera.takeImageFromCamera() }

            mViewModel.user.observe(this, Observer {
                it?.let {
                    mUser = it
                    name_input.setText(mUser.name, TextView.BufferType.EDITABLE)
                    username_input.setText(mUser.username, TextView.BufferType.EDITABLE)
                    website_input.setText(mUser.website, TextView.BufferType.EDITABLE)
                    bio_input.setText(mUser.bio, TextView.BufferType.EDITABLE)
                    email_input.setText(mUser.email, TextView.BufferType.EDITABLE)
                    phone_input.setText(mUser.phone, TextView.BufferType.EDITABLE)
                    profile_image_edit.loadUserPhoto(mUser.photo)
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mCamera.requestCode && resultCode == Activity.RESULT_OK) {
            mViewModel.uploadAndSetUserPhoto(mCamera.mImageUri!!)
        }
    }

    private fun updateProfile() {
        mPendingUser = User(
            name_input.text.toString(),
            username_input.text.toString(),
            website_input.text.toStringOrNull(),
            bio_input.text.toStringOrNull(),
            email_input.text.toString(),
            phone_input.text.toStringOrNull()
        )
        val error = validate(mPendingUser)
        if (error == null) {
            if (mPendingUser.email == mUser.email) {
                updateUser(mPendingUser)
            } else {
                @Suppress("DEPRECATION")
                PasswordDialog().show(fragmentManager, "password_dialog")
            }
        } else {
            showToast(error)
        }
    }


    private fun updateUser(user: User) {
        mViewModel.updateUserProfile(currentUser = mUser, newUser = user)
            .addOnSuccessListener {
                showToast(getString(R.string.profile_saved))
                finish()
            }
    }

    private fun validate(user: User): String? {
        return when {
            user.name.isEmpty() -> getString(R.string.please_enter_name)
            user.username.isEmpty() -> getString(R.string.please_enter_username)
            user.email.isEmpty() -> getString(R.string.please_enter_email)
            else -> null
        }
    }

    override fun onPasswordConfirm(password: String) {
        if (password.isNotEmpty()) {
            mViewModel.updateEmail(
                currentEmail = mUser.email,
                newEmail = mPendingUser.email,
                password = password
            ).addOnSuccessListener { updateUser(mPendingUser) }
        } else {
            showToast(getString(R.string.you_should_enter_pass))
        }
    }

    companion object {
        const val tag = "EditProfileActivity"
    }
}