package vanson.dev.instagramclone.controllers

import android.app.Application
import vanson.dev.instagramclone.controllers.notification.NotificationsCreator
import vanson.dev.instagramclone.controllers.search.SearchPostsCreator
import vanson.dev.instagramclone.repository.firebase.FirebaseFeedPostsRepository
import vanson.dev.instagramclone.repository.firebase.FirebaseNotificationsRepository
import vanson.dev.instagramclone.repository.firebase.FirebaseSearchRepository
import vanson.dev.instagramclone.repository.firebase.FirebaseUsersRepository
import vanson.dev.instagramclone.utilites.firebase.FirebaseAuthManager

class InstagramApp : Application() { //first init of application
    val usersRepo by lazy { FirebaseUsersRepository() }
    val feedPostsRepo by lazy { FirebaseFeedPostsRepository() }
    val authManger by lazy { FirebaseAuthManager() }
    val notificationsRepo by lazy { FirebaseNotificationsRepository() }
    val searchPostsRepo by lazy { FirebaseSearchRepository() }

    override fun onCreate() {
        super.onCreate()
        NotificationsCreator(notificationsRepo, usersRepo, feedPostsRepo)
        SearchPostsCreator(searchPostsRepo)
    }
}