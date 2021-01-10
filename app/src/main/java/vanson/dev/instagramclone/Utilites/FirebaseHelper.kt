package vanson.dev.instagramclone.Utilites

import android.app.Activity
import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import vanson.dev.instagramclone.showToast

class FirebaseHelper(private val activity: Activity){
    private var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var mStorage: StorageReference = FirebaseStorage.getInstance().reference
    private var mAuth: FirebaseAuth =
        FirebaseAuth.getInstance()

    fun reauthenticate(
        credentials: AuthCredential,
        onSuccess: () -> Unit
    ) { //Note!!!
        mAuth.currentUser!!.reauthenticate(credentials).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

    fun updateEmail(email: String, onSuccess: () -> Unit) {
        mAuth.currentUser!!.updateEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

    fun updateUser(
        userMap: Map<String, Any?>,
        onSuccess: () -> Unit
    ) {
        mDatabase.child("Users").child(mAuth.currentUser!!.uid).updateChildren(userMap).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
              activity.showToast(it.exception!!.message!!)
            }
        }
    }

    fun uploadUserPhoto(
        image: Uri,
        onSuccess: (Uri) -> Unit
    ) {
        mStorage.child("Users/${mAuth.currentUser!!.uid}/photo").putFile(image).continueWithTask { task ->
            if (!task.isSuccessful) {
                activity.showToast(task.exception!!.message!!)
            }
            mStorage.child("Users/${mAuth.currentUser!!.uid}/photo").downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess(it.result!!)
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

    fun uploadUserPhoto(
        photoUrl: String,
        onSuccess: () -> Unit
    ) {
        mDatabase.child("Users/${mAuth.currentUser!!.uid}/photo").setValue(photoUrl).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                activity.showToast(it.exception!!.message!!)
            }
        }
    }

    fun currentUserReference(): DatabaseReference = mDatabase.child("Users").child(mAuth.currentUser!!.uid)
}