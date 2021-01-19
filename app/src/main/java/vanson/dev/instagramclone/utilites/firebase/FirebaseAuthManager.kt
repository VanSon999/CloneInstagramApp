package vanson.dev.instagramclone.utilites.firebase

import vanson.dev.instagramclone.repository.firebase.common.auth
import vanson.dev.instagramclone.utilites.AuthManager

class FirebaseAuthManager : AuthManager {
    override fun signOut() {
        auth.signOut()
    }
}