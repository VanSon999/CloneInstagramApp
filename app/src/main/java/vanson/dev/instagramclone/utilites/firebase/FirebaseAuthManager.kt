package vanson.dev.instagramclone.utilites.firebase

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import vanson.dev.instagramclone.repository.firebase.common.auth
import vanson.dev.instagramclone.utilites.AuthManager

class FirebaseAuthManager : AuthManager {
    override fun signOut() {
        auth.signOut()
    }

    override fun signIn(email: String, password: String): Task<Unit> =
        auth.signInWithEmailAndPassword(email, password).onSuccessTask { Tasks.forResult(Unit) }
}