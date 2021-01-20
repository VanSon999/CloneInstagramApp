package vanson.dev.instagramclone.controllers.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import vanson.dev.instagramclone.repository.UsersRepository

class ProfileViewModel(private val usersRepo: UsersRepository) : ViewModel() {
    val user = usersRepo.getUser()
    lateinit var images: LiveData<List<String>>

    fun init(uid: String) {
        images = usersRepo.getListImagesOfUser(uid)
    }
}
