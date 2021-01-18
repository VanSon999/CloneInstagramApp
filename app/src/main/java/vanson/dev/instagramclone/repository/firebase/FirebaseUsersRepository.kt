package vanson.dev.instagramclone.repository.firebase

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import vanson.dev.instagramclone.utilites.toUnit
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.repository.UsersRepository
import vanson.dev.instagramclone.utilites.*
import vanson.dev.instagramclone.asUser
import vanson.dev.instagramclone.mapCustom

class FirebaseUsersRepository : UsersRepository {
    override fun getUser(): LiveData<User> =
        database.child("Users").child(currentUid()!!).liveData().mapCustom {
            it.asUser()!!
        }

    override fun getUsers(): LiveData<List<User>> =
        database.child("Users").liveData().mapCustom { dataSnapshot ->
            dataSnapshot.children.map { it.asUser()!! }
        }


    override fun addFollow(fromUid: String, toUid: String): Task<Unit> =
        getFollowsRef(fromUid, toUid).setValue(true).toUnit()


    override fun deleteFollow(fromUid: String, toUid: String): Task<Unit> =
        getFollowsRef(fromUid, toUid).removeValue().toUnit()


    override fun addFollower(fromUid: String, toUid: String): Task<Unit> =
        getFollowersRef(fromUid, toUid).setValue(true).toUnit()

    override fun deleteFollower(fromUid: String, toUid: String): Task<Unit> =
        getFollowersRef(fromUid, toUid).removeValue().toUnit()


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

    private fun getFollowsRef(fromUid: String, toUid: String) =
        database.child("Users").child(fromUid).child("Follows").child(toUid)

    private fun getFollowersRef(fromUid: String, toUid: String) =
        database.child("Users").child(toUid).child("Followers").child(fromUid)

    override fun currentUid(): String? = FirebaseAuth.getInstance().currentUser?.uid
}