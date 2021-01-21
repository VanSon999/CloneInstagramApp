package vanson.dev.instagramclone.controllers.profile

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.controllers.common.BaseViewModel
import vanson.dev.instagramclone.repository.UsersRepository

class ProfileViewModel(
    private val usersRepo: UsersRepository,
    onFailureListener: OnFailureListener
) : BaseViewModel(onFailureListener) {
    val user = usersRepo.getUser()
    lateinit var images: LiveData<List<String>>

    fun init(uid: String) {
        images = usersRepo.getListImagesOfUser(uid)
    }
}
