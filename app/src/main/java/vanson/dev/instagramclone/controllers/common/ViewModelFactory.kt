package vanson.dev.instagramclone.controllers.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.controllers.addfriends.AddFriendsViewModel
import vanson.dev.instagramclone.repository.firebase.FirebaseFeedPostsRepository
import vanson.dev.instagramclone.controllers.editprofile.EditProfileViewModel
import vanson.dev.instagramclone.controllers.home.HomeViewModel
import vanson.dev.instagramclone.controllers.profilesettings.ProfileSettingViewModel
import vanson.dev.instagramclone.repository.FeedPostsRepository
import vanson.dev.instagramclone.repository.firebase.FirebaseUsersRepository
import vanson.dev.instagramclone.utilites.firebase.FirebaseAuthManager

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val onFailureListener: OnFailureListener) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        with(modelClass) {
            return when {
                isAssignableFrom(AddFriendsViewModel::class.java) -> {
                    AddFriendsViewModel(
                        onFailureListener,
                        FirebaseUsersRepository(),
                        FirebaseFeedPostsRepository()
                    ) as T
                }
                isAssignableFrom(EditProfileViewModel::class.java) -> {
                    EditProfileViewModel(onFailureListener, FirebaseUsersRepository()) as T
                }
                isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(onFailureListener, FirebaseFeedPostsRepository()) as T
                }
                isAssignableFrom(ProfileSettingViewModel::class.java) -> {
                    ProfileSettingViewModel(FirebaseAuthManager()) as T
                }
                else -> {
                    error("Unknown view model class $modelClass")
                }
            }
        }
    }
}