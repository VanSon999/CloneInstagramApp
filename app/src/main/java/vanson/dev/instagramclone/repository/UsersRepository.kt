package vanson.dev.instagramclone.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import vanson.dev.instagramclone.models.FeedPost
import vanson.dev.instagramclone.models.User

interface UsersRepository {
    fun getUser(): LiveData<User>
    fun getUsers(): LiveData<List<User>>
    fun currentUid(): String?
    fun addFollow(fromUid: String, toUid: String): Task<Unit>
    fun deleteFollow(fromUid: String, toUid: String): Task<Unit>
    fun addFollower(fromUid: String, toUid: String): Task<Unit>
    fun deleteFollower(fromUid: String, toUid: String): Task<Unit>
    fun uploadUserPhoto(localImage: Uri): Task<Uri>
    fun updateUserPhoto(downloadUrl: Uri): Task<Unit>
    fun updateEmail(currentEmail: String, newEmail: String, password: String): Task<Unit>
    fun updateUserProfile(currentUser: User, newUser: User): Task<Unit>
    fun getListImagesOfUser(uid: String): LiveData<List<String>>
    fun isUserExistsForEmail(email: String): Task<Boolean>
    fun createUser(user: User, password: String): Task<Unit>
    fun uploadUserImage(uid: String, uriImage: Uri): Task<Uri>
    fun setUserImage(uid: String, imageUri: Uri): Task<Unit>
}