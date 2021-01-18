package vanson.dev.instagramclone.controllers.editprofile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.repository.UsersRepository

class EditProfileViewModel(
    private val onFailureListener: OnFailureListener,
    private val repository: UsersRepository
) : ViewModel() {
    val user: LiveData<User> = repository.getUser()

    fun uploadAndSetUserPhoto(localImage: Uri): Task<Unit> =
        repository.uploadUserPhoto(localImage).onSuccessTask { downloadUrl ->
            repository.updateUserPhoto(downloadUrl!!)
        }.addOnFailureListener(onFailureListener)

    fun updateEmail(currentEmail: String, newEmail: String, password: String): Task<Unit> =
        repository.updateEmail(
            currentEmail = currentEmail,
            newEmail = newEmail,
            password = password
        ).addOnFailureListener(onFailureListener)

    fun updateUserProfile(currentUser: User, newUser: User): Task<Unit> =
        repository.updateUserProfile(currentUser = currentUser, newUser = newUser)
            .addOnFailureListener(onFailureListener)
}