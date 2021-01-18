package vanson.dev.instagramclone.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vanson.dev.instagramclone.controllers.addfriends.AddFriendsViewModel
import vanson.dev.instagramclone.repository.firebase.FirebaseFeedPostsRepository
import vanson.dev.instagramclone.controllers.editprofile.EditProfileViewModel
import vanson.dev.instagramclone.repository.firebase.FirebaseUsersRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        with(modelClass) {
            when {
                isAssignableFrom(AddFriendsViewModel::class.java) -> {
                    return AddFriendsViewModel(FirebaseUsersRepository(),FirebaseFeedPostsRepository()) as T
                }
                isAssignableFrom(EditProfileViewModel::class.java) -> {
                    return EditProfileViewModel(FirebaseUsersRepository()) as T
                }
                else -> {
                    error("Unknown view model class $modelClass")
                }
            }
        }
    }
}