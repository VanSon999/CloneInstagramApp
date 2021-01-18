package vanson.dev.instagramclone.Controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vanson.dev.instagramclone.Controllers.addfriends.AddFriendsActivity
import vanson.dev.instagramclone.Controllers.addfriends.AddFriendsViewModel
import vanson.dev.instagramclone.Controllers.addfriends.FirebaseAddFriendsRepository
import vanson.dev.instagramclone.Controllers.editprofile.EditProfileViewModel
import vanson.dev.instagramclone.Controllers.editprofile.FirebaseEditProfileRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        with(modelClass) {
            when {
                isAssignableFrom(AddFriendsViewModel::class.java) -> {
                    return AddFriendsViewModel(FirebaseAddFriendsRepository()) as T
                }
                isAssignableFrom(EditProfileViewModel::class.java) -> {
                    return EditProfileViewModel(FirebaseEditProfileRepository()) as T
                }
                else -> {
                    error("Unknown view model class $modelClass")
                }
            }
        }
    }
}