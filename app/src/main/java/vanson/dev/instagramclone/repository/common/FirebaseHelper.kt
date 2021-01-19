package vanson.dev.instagramclone.repository.common

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirebaseHelper(private val activity: Activity) {
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val storage: StorageReference = FirebaseStorage.getInstance().reference
    val auth: FirebaseAuth = FirebaseAuth.getInstance()




    fun currentUserReference(): DatabaseReference =
        database.child("Users").child(currentUid()!!)

    fun currentUid(): String? = auth.currentUser?.uid
}
//    fun updateUser(
//        userMap: Map<String, Any?>,
//        onSuccess: () -> Unit
//    ) {
//        database.child("Users").child(currentUid()!!).updateChildren(userMap)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    onSuccess()
//                } else {
//                    activity.showToast(it.exception!!.message!!)
//                }
//            }
//    }
//    fun uploadUserPhoto(
//        image: Uri,
//        onSuccess: (Uri) -> Unit
//    ) {
//        storage.child("Users/${currentUid()!!}/photo").putFile(image)
//            .continueWithTask { task ->
//                if (!task.isSuccessful) {
//                    activity.showToast(task.exception!!.message!!)
//                }
//                storage.child("Users/${currentUid()!!}/photo").downloadUrl
//            }.addOnCompleteListener {
//            if (it.isSuccessful) {
//                onSuccess(it.result!!)
//            } else {
//                activity.showToast(it.exception!!.message!!)
//            }
//        }
//    }
//
//    fun uploadUserPhoto(
//        photoUrl: String,
//        onSuccess: () -> Unit
//    ) {
//        database.child("Users/${currentUid()!!}/photo").setValue(photoUrl)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//                    onSuccess()
//                } else {
//                    activity.showToast(it.exception!!.message!!)
//                }
//            }
//    }
//    fun reauthenticate(
//        credentials: AuthCredential,
//        onSuccess: () -> Unit
//    ) { //Note!!!
//        auth.currentUser!!.reauthenticate(credentials).addOnCompleteListener {
//            if (it.isSuccessful) {
//                onSuccess()
//            } else {
//                activity.showToast(it.exception!!.message!!)
//            }
//        }
//    }
//
//    fun updateEmail(email: String, onSuccess: () -> Unit) {
//        auth.currentUser!!.updateEmail(email).addOnCompleteListener {
//            if (it.isSuccessful) {
//                onSuccess()
//            } else {
//                activity.showToast(it.exception!!.message!!)
//            }
//        }
//    }
