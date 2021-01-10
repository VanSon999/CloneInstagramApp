package vanson.dev.instagramclone.Controllers

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import vanson.dev.instagramclone.*
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.Utilites.CameraHelper
import vanson.dev.instagramclone.Utilites.FirebaseHelper
import vanson.dev.instagramclone.Utilites.ValueEventListenerAdapter
import vanson.dev.instagramclone.Views.PasswordDialog

class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener {

    private lateinit var mPendingUser: User
    private lateinit var mUser: User
    private lateinit var cameraHelper: CameraHelper
    private lateinit var mFirebaseHelper : FirebaseHelper

    private val TAG = "EditProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(TAG, "onCreate")
        cameraHelper =
            CameraHelper(this)
        mFirebaseHelper = FirebaseHelper(this)

        close_image.setOnClickListener {
            finish()
        }
        save_image.setOnClickListener { updateProfile() }
        change_avatar_text.setOnClickListener { cameraHelper.takeImageFromCamera() }

        mFirebaseHelper.currentUserReference()
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.getValue(User::class.java)!!
                name_input.setText(mUser!!.name, TextView.BufferType.EDITABLE)
                username_input.setText(mUser!!.username, TextView.BufferType.EDITABLE)
                website_input.setText(mUser!!.website, TextView.BufferType.EDITABLE)
                bio_input.setText(mUser!!.bio, TextView.BufferType.EDITABLE)
                email_input.setText(mUser!!.email, TextView.BufferType.EDITABLE)
                phone_input.setText(mUser!!.phone, TextView.BufferType.EDITABLE)
                profile_image_edit.loadUserPhoto(mUser.photo)
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraHelper.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mFirebaseHelper.uploadUserPhoto(cameraHelper.mImageUri!!) {
                val photoUrl = it.toString()
                mFirebaseHelper.uploadUserPhoto(photoUrl) {
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

        mFirebaseHelper.updateUser(updateMap) {
            showToast("Profile saved!")
            finish()
        }
    }

    private fun validate(user: User): String? {
        return when {
            user.name.isEmpty() -> "Please enter name"
            user.username.isEmpty() -> "Please enter username"
            user.email.isEmpty() -> "Please enter email"
            else -> null
        }
    }

    override fun onPasswordConfirm(password: String) {
        if (password.isNotEmpty()) {
            val credentials = EmailAuthProvider.getCredential(mUser.email, password)
            mFirebaseHelper.reauthenticate(credentials) {
                mFirebaseHelper.updateEmail(mPendingUser.email) {
                    updateUser(mPendingUser)
                }
            }
        } else {
            showToast("You should enter password!")
        }
    }
}