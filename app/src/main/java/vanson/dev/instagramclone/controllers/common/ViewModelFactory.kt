package vanson.dev.instagramclone.controllers.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.controllers.addfriends.AddFriendsViewModel
import vanson.dev.instagramclone.repository.firebase.FirebaseFeedPostsRepository
import vanson.dev.instagramclone.controllers.editprofile.EditProfileViewModel
import vanson.dev.instagramclone.repository.firebase.FirebaseUsersRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val onFailureListener: OnFailureListener) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        with(modelClass) {
            when {
                isAssignableFrom(AddFriendsViewModel::class.java) -> {
                    return AddFriendsViewModel(
                        onFailureListener,
                        FirebaseUsersRepository(),
                        FirebaseFeedPostsRepository()
                    ) as T
                }
                isAssignableFrom(EditProfileViewModel::class.java) -> {
                    return EditProfileViewModel(onFailureListener, FirebaseUsersRepository()) as T
                }
                else -> {
                    error("Unknown view model class $modelClass")
                }
            }
        }
    }
}