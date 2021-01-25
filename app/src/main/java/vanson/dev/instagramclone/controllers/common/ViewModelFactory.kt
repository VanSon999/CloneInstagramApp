package vanson.dev.instagramclone.controllers.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.controllers.InstagramApp
import vanson.dev.instagramclone.controllers.search.SearchViewModel
import vanson.dev.instagramclone.controllers.addfriends.AddFriendsViewModel
import vanson.dev.instagramclone.controllers.comments.CommentsViewModel
import vanson.dev.instagramclone.controllers.editprofile.EditProfileViewModel
import vanson.dev.instagramclone.controllers.home.HomeViewModel
import vanson.dev.instagramclone.controllers.login.LoginViewModel
import vanson.dev.instagramclone.controllers.notification.NotificationsViewModel
import vanson.dev.instagramclone.controllers.profile.ProfileViewModel
import vanson.dev.instagramclone.controllers.profilesettings.ProfileSettingViewModel
import vanson.dev.instagramclone.controllers.register.RegisterViewModel
import vanson.dev.instagramclone.controllers.share.ShareViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val app: InstagramApp,
    private val commonViewModel: CommonViewModel,
    private val onFailureListener: OnFailureListener
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        with(modelClass) {
            val usersRepo = app.usersRepo
            val feedPostsRepo = app.feedPostsRepo
            val authManger = app.authManger
            val notificationsRepo = app.notificationsRepo
            val searchPostsRepo = app.searchPostsRepo
            return when {
                isAssignableFrom(AddFriendsViewModel::class.java) -> {
                    AddFriendsViewModel(onFailureListener, usersRepo, feedPostsRepo) as T
                }
                isAssignableFrom(EditProfileViewModel::class.java) -> {
                    EditProfileViewModel(onFailureListener, usersRepo) as T
                }
                isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(onFailureListener, feedPostsRepo) as T
                }
                isAssignableFrom(ProfileSettingViewModel::class.java) -> {
                    ProfileSettingViewModel(authManger, onFailureListener) as T
                }
                isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(authManger, app, commonViewModel, onFailureListener) as T
                }
                isAssignableFrom(ProfileViewModel::class.java) -> {
                    ProfileViewModel(usersRepo, onFailureListener) as T
                }
                isAssignableFrom(RegisterViewModel::class.java) -> {
                    RegisterViewModel(app, usersRepo, commonViewModel, onFailureListener) as T
                }
                isAssignableFrom(ShareViewModel::class.java) -> {
                    ShareViewModel(usersRepo, feedPostsRepo, onFailureListener) as T
                }
                isAssignableFrom(CommentsViewModel::class.java) -> {
                    CommentsViewModel(feedPostsRepo, usersRepo, onFailureListener) as T
                }
                isAssignableFrom(NotificationsViewModel::class.java) -> {
                    NotificationsViewModel(notificationsRepo, onFailureListener) as T
                }
                isAssignableFrom(SearchViewModel::class.java) -> {
                    SearchViewModel(searchPostsRepo,onFailureListener) as T
                }
                else -> {
                    error("Unknown view model class $modelClass")
                }
            }
        }
    }
}