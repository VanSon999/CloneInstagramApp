package vanson.dev.instagramclone.Controllers.editprofile

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.EmailAuthProvider
import vanson.dev.instagramclone.Controllers.addfriends.toUnit
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.Utilites.*
import vanson.dev.instagramclone.asUser
import vanson.dev.instagramclone.mapCustom
import vanson.dev.instagramclone.showToast

interface EditProfileRepository {
    fun getUser(): LiveData<User>
    fun uploadUserPhoto(localImage: Uri): Task<Uri>
    fun updateUserPhoto(downloadUrl: Uri): Task<Unit>
    fun updateEmail(currentEmail: String, newEmail: String, password: String): Task<Unit>
    fun updateUserProfile(currentUser: User, newUser: User): Task<Unit>
}

class FirebaseEditProfileRepository : EditProfileRepository {
    override fun getUser(): LiveData<User> =
        database.child("Users").child(currentUid()!!).liveData().mapCustom {
            it.asUser()!!
        }

    override fun uploadUserPhoto(localImage: Uri): Task<Uri> =
        storage.child("Users/${currentUid()!!}/photo").putFile(localImage)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    error(task.exception!!.message!!)
                }
                storage.child("Users/${currentUid()!!}/photo").downloadUrl
            }

    override fun updateUserPhoto(downloadUrl: Uri): Task<Unit> =
        database.child("Users/${currentUid()!!}/photo").setValue(downloadUrl.toString()).toUnit()

    override fun updateEmail(currentEmail: String, newEmail: String, password: String): Task<Unit> {
        val credentials = EmailAuthProvider.getCredential(currentEmail, password)
        val currentUser = auth.currentUser
        return if (currentUser != null) {
            currentUser.reauthenticate(credentials).onSuccessTask {
                currentUser.updateEmail(newEmail)
            }.toUnit()
        }else{
            Tasks.forException<Unit>(IllegalStateException("User is not authenticated"))
        }
    }

    override fun updateUserProfile(currentUser: User, newUser: User): Task<Unit> {
        val updateMap = mutableMapOf<String, Any?>()
        if (newUser.name != currentUser.name) updateMap["name"] = newUser.name
        if (newUser.username != currentUser.username) updateMap["username"] = newUser.username
        if (newUser.email != currentUser.email) updateMap["email"] = newUser.email
        if (newUser.bio != currentUser.bio) updateMap["bio"] = newUser.bio
        if (newUser.website != currentUser.website) updateMap["website"] = newUser.website
        if (newUser.phone != currentUser.phone) updateMap["phone"] = newUser.phone

        return database.child("Users").child(currentUid()!!).updateChildren(updateMap).toUnit()
    }
}