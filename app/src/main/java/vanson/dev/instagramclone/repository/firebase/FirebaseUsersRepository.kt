package vanson.dev.instagramclone.repository.firebase

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.repository.UsersRepository
import vanson.dev.instagramclone.repository.common.liveData
import vanson.dev.instagramclone.repository.common.mapCustom
import vanson.dev.instagramclone.repository.firebase.common.auth
import vanson.dev.instagramclone.repository.firebase.common.database
import vanson.dev.instagramclone.repository.firebase.common.storage
import vanson.dev.instagramclone.utilites.Event
import vanson.dev.instagramclone.utilites.EventBus
import vanson.dev.instagramclone.utilites.task
import vanson.dev.instagramclone.utilites.toUnit

class FirebaseUsersRepository : UsersRepository {
    override fun getUser(): LiveData<User> = getUser(currentUid()!!)

    override fun getUser(uid: String): LiveData<User> =
        database.child("Users").child(currentUid()!!).liveData().mapCustom {
            it.asUser()!!
        }

    override fun getUsers(): LiveData<List<User>> =
        database.child("Users").liveData().mapCustom { dataSnapshot ->
            dataSnapshot.children.map { it.asUser()!! }
        }


    override fun addFollow(fromUid: String, toUid: String): Task<Unit> =
        getFollowsRef(fromUid, toUid).setValue(true).toUnit().addOnSuccessListener {
            EventBus.publish(Event.CreateFollow(fromUid, toUid))
        }


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
        } else {
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

    override fun getListImagesOfUser(uid: String): LiveData<List<String>> =
        database.child("images").child(uid).liveData().mapCustom { dataSnapshot ->
            dataSnapshot.children.map { it.getValue(String::class.java)!! }
        }

    override fun isUserExistsForEmail(email: String): Task<Boolean> =
        auth.fetchSignInMethodsForEmail(email).onSuccessTask {
            val signInMethod = it?.signInMethods ?: emptyList<String>()
            Tasks.forResult(signInMethod.isNotEmpty())
        }

    override fun createUser(user: User, password: String): Task<Unit> =
        auth.createUserWithEmailAndPassword(user.email, password).onSuccessTask {
            database.child("Users").child(it!!.user!!.uid).setValue(user)
        }.toUnit()

    override fun uploadUserImage(uid: String, uriImage: Uri): Task<Uri> =
        task { taskSource ->
            storage.child("Users").child(uid).child("images").child(
                uriImage.lastPathSegment!!
            ).putFile(uriImage).continueWithTask { task ->
                if (!task.isSuccessful) {
                    error(task.exception!!.message!!)
                }
                storage.child("Users").child(uid).child("images")
                    .child(uriImage.lastPathSegment!!).downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    taskSource.setResult(it.result)
                } else {
                    taskSource.setException(it.exception!!)
                }
            }
        }

    override fun setUserImage(uid: String, imageUri: Uri): Task<Unit> =
        database.child("images").child(uid).push().setValue(imageUri.toString()).toUnit()

    private fun getFollowsRef(fromUid: String, toUid: String) =
        database.child("Users").child(fromUid).child("Follows").child(toUid)

    private fun getFollowersRef(fromUid: String, toUid: String) =
        database.child("Users").child(toUid).child("Followers").child(fromUid)

    override fun currentUid(): String? = FirebaseAuth.getInstance().currentUser?.uid

    private fun DataSnapshot.asUser(): User? = getValue(User::class.java)?.copy(uid = key.toString())
}