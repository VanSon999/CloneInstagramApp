package vanson.dev.instagramclone.Controllers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.ValueEventListenerAdapter
import vanson.dev.instagramclone.Views.PasswordDialog
import vanson.dev.instagramclone.showToast

class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener {
    private lateinit var mPendingUser: User
    private lateinit var mDatabase: DatabaseReference
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUser: User

    private val TAG = "EditProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        Log.d(TAG, "onCreate")

        close_image.setOnClickListener {
            finish()
        }
        save_image.setOnClickListener { updateProfile() }

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("Users").child(mAuth.currentUser!!.uid)
            .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.getValue(User::class.java)!!
                name_input.setText(mUser!!.name, TextView.BufferType.EDITABLE)
                username_input.setText(mUser!!.username, TextView.BufferType.EDITABLE)
                website_input.setText(mUser!!.website, TextView.BufferType.EDITABLE)
                bio_input.setText(mUser!!.bio, TextView.BufferType.EDITABLE)
                email_input.setText(mUser!!.email, TextView.BufferType.EDITABLE)
                phone_input.setText(mUser!!.phone, TextView.BufferType.EDITABLE)
            })
    }

    private fun updateProfile() {
         mPendingUser = User(
            name_input.text.toString(),
            username_input.text.toString(),
            website_input.text.toString(),
            bio_input.text.toString(),
            email_input.text.toString(),
            phone_input.text.toString()
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
        val updateMap = mutableMapOf<String, Any>()
        if (user.name != mUser.name) updateMap["name"] = user.name
        if (user.username != mUser.username) updateMap["username"] = user.username
        if (user.email != mUser.email) updateMap["email"] = user.email
        if (user.bio != mUser.bio) updateMap["bio"] = user.bio
        if (user.website != mUser.website) updateMap["website"] = user.website
        if (user.phone != mUser.phone) updateMap["phone"] = user.phone

        mDatabase.child("Users").child(mAuth.currentUser!!.uid).updateChildren(updateMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast("Profile saved!")
                    finish()
                } else {
                    showToast(it.exception!!.message!!)
                }
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
        if(password.isNotEmpty()){
            val credentials = EmailAuthProvider.getCredential(mUser.email, password)
            mAuth.currentUser!!.reauthenticate(credentials).addOnCompleteListener {
                if(it.isSuccessful){
                    mAuth.currentUser!!.updateEmail(mPendingUser.email).addOnCompleteListener {
                        if(it.isSuccessful){
                            updateUser(mPendingUser)
                        }else{
                            showToast(it.exception!!.message!!)
                        }
                    }
                }else{
                    showToast(it.exception!!.message!!)
                }
            }
        }else{
            showToast("You should enter password!")
        }
    }
}