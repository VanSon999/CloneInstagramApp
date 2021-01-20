package vanson.dev.instagramclone.controllers.common

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.controllers.LoginViewModel
import vanson.dev.instagramclone.controllers.addfriends.AddFriendsViewModel
import vanson.dev.instagramclone.controllers.editprofile.EditProfileViewModel
import vanson.dev.instagramclone.controllers.home.HomeViewModel
import vanson.dev.instagramclone.controllers.profilesettings.ProfileSettingViewModel
import vanson.dev.instagramclone.repository.firebase.FirebaseFeedPostsRepository
import vanson.dev.instagramclone.repository.firebase.FirebaseUsersRepository
import vanson.dev.instagramclone.utilites.firebase.FirebaseAuthManager

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val app: Application,
    private val commonViewModel: CommonViewModel,
    private val onFailureListener: OnFailureListener
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        with(modelClass) {
            val usersRepo by lazy { FirebaseUsersRepository() }
            val feedPostsRepo by lazy { FirebaseFeedPostsRepository() }
            val authManger by lazy { FirebaseAuthManager() }
            return when {
                isAssignableFrom(AddFriendsViewModel::class.java) -> {
                    AddFriendsViewModel(
                        onFailureListener,
                        usersRepo,
                        feedPostsRepo
                    ) as T
                }
                isAssignableFrom(EditProfileViewModel::class.java) -> {
                    EditProfileViewModel(onFailureListener, usersRepo) as T
                }
                isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(onFailureListener, feedPostsRepo) as T
                }
                isAssignableFrom(ProfileSettingViewModel::class.java) -> {
                    ProfileSettingViewModel(authManger) as T
                }
                isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(
                        authManger,
                        app,
                        commonViewModel,
                        onFailureListener
                    ) as T
                }
                else -> {
                    error("Unknown view model class $modelClass")
                }
            }
        }
    }
}