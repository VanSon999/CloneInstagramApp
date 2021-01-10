package vanson.dev.instagramclone.Controllers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.FileProvider
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_edit_profile.*
import vanson.dev.instagramclone.*
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.Views.PasswordDialog
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener {
    private lateinit var mImageUri: Uri
    private val TAKE_PICTURE_REQUEST_CODE = 1
    private lateinit var mPendingUser: User
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mStorage: StorageReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUser: User
    val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)


    private val TAG = "EditProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(TAG, "onCreate")

        close_image.setOnClickListener {
            finish()
        }
        save_image.setOnClickListener { updateProfile() }
        change_avatar_text.setOnClickListener { takeImageFromCamera() }
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mStorage = FirebaseStorage.getInstance().reference
        mDatabase.child("Users").child(mAuth.currentUser!!.uid)
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
        if (requestCode == TAKE_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uid = mAuth.currentUser!!.uid
            val fileRef = mStorage.child("Users/$uid/photo")
            fileRef.putFile(mImageUri).continueWithTask{ task ->
                if (!task.isSuccessful) {
                    showToast(task.exception!!.message!!)
                }
                fileRef.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    val photoUrl = it.result.toString()
                    mDatabase.child("Users/$uid/photo").setValue(photoUrl)
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                mUser = mUser.copy(photo = photoUrl)
                                profile_image_edit.loadUserPhoto(mUser.photo)
                            }else{
                                showToast(it.exception!!.message!!)
                            }
                        }
                } else {
                    showToast(it.exception!!.message!!)
                }
            }
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${simpleDateFormat.format(Date())}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    private fun takeImageFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {//check camera on device
            val imageFile = createImageFile()
            mImageUri = FileProvider.getUriForFile(
                this,
                "vanson.dev.instagramclone.fileprovider",
                imageFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
            startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE)
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

        mDatabase.updateUser(mAuth.currentUser!!.uid, updateMap) {
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
            mAuth.currentUser!!.reauthenticate(credentials) {
                mAuth.currentUser!!.updateEmail(mPendingUser.email) {
                    updateUser(mPendingUser)
                }
            }
        } else {
            showToast("You should enter password!")
        }
    }

    private fun FirebaseUser.reauthenticate(
        credentials: AuthCredential,
        onSuccess: () -> Unit
    ) { //Note!!!
        reauthenticate(credentials).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                showToast(it.exception!!.message!!)
            }
        }
    }

    private fun FirebaseUser.updateEmail(email: String, onSuccess: () -> Unit) {
        updateEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                showToast(it.exception!!.message!!)
            }
        }
    }

    private fun DatabaseReference.updateUser(
        uid: String,
        userMap: Map<String, Any?>,
        onSuccess: () -> Unit
    ) {
        child("Users").child(uid).updateChildren(userMap).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                showToast(it.exception!!.message!!)
            }
        }
    }
}