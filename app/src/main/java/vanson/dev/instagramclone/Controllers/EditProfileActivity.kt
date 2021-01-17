package vanson.dev.instagramclone.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.android.synthetic.main.activity_edit_profile.*
import vanson.dev.instagramclone.*
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.Utilites.CameraHelper
import vanson.dev.instagramclone.Utilites.FirebaseHelper
import vanson.dev.instagramclone.Utilites.ValueEventListenerAdapter
import vanson.dev.instagramclone.Views.PasswordDialog

class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener {

    private lateinit var mPendingUser: User
    private lateinit var mUser: User
    private lateinit var mCamera: CameraHelper
    private lateinit var mFirebase : FirebaseHelper

    private val tag = "EditProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(tag, "onCreate")
        mCamera =
            CameraHelper(this)
        mFirebase = FirebaseHelper(this)

        close_image.setOnClickListener {
            finish()
        }
        save_image.setOnClickListener { updateProfile() }
        change_avatar_text.setOnClickListener { mCamera.takeImageFromCamera() }

        mFirebase.currentUserReference()
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.asUser()!!
                name_input.setText(mUser.name, TextView.BufferType.EDITABLE)
                username_input.setText(mUser.username, TextView.BufferType.EDITABLE)
                website_input.setText(mUser.website, TextView.BufferType.EDITABLE)
                bio_input.setText(mUser.bio, TextView.BufferType.EDITABLE)
                email_input.setText(mUser.email, TextView.BufferType.EDITABLE)
                phone_input.setText(mUser.phone, TextView.BufferType.EDITABLE)
                profile_image_edit.loadUserPhoto(mUser.photo)
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mCamera.requestCode && resultCode == Activity.RESULT_OK) {
            mFirebase.uploadUserPhoto(mCamera.mImageUri!!) {
                val photoUrl = it.toString()
                mFirebase.uploadUserPhoto(photoUrl) {
                    mUser = mUser.copy(photo = photoUrl)
                    profile_image_edit.loadUserPhoto(mUser.photo)
                }
            }
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
        val updateMap = mutableMapOf<String, Any?>()
        if (user.name != mUser.name) updateMap["name"] = user.name
        if (user.username != mUser.username) updateMap["username"] = user.username
        if (user.email != mUser.email) updateMap["email"] = user.email
        if (user.bio != mUser.bio) updateMap["bio"] = user.bio
        if (user.website != mUser.website) updateMap["website"] = user.website
        if (user.phone != mUser.phone) updateMap["phone"] = user.phone

        mFirebase.updateUser(updateMap) {
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
            val credentials = EmailAuthProvider.getCredential(mUser.email, password)
            mFirebase.reauthenticate(credentials) {
                mFirebase.updateEmail(mPendingUser.email) {
                    updateUser(mPendingUser)
                }
            }
        } else {
            showToast(getString(R.string.you_should_enter_pass))
        }
    }
}