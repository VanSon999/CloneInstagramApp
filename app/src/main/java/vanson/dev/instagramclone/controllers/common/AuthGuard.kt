package vanson.dev.instagramclone.controllers.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.firebase.auth.FirebaseAuth
import vanson.dev.instagramclone.repository.firebase.common.auth

class AuthGuard(private val activity: BaseActivity, private val f: (String) -> Unit) :LifecycleObserver {
    init {
        val user = auth.currentUser
        if(user == null){
            activity.goToLogin()
        }else{
            f(user.uid)
            activity.lifecycle.addObserver(this)
        }
    }

    private val listener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {
        if(it.currentUser == null){
            activity.goToLogin()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop(){
        auth.removeAuthStateListener(listener)
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        auth.addAuthStateListener(listener)
    }
}

fun BaseActivity.setupAuthGuard(f: (String) -> Unit){
    AuthGuard(this, f)
}