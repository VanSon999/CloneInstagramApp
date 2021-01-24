package vanson.dev.instagramclone.controllers.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.BaseViewModel
import vanson.dev.instagramclone.controllers.common.CommonViewModel
import vanson.dev.instagramclone.utilites.AuthManager
import vanson.dev.instagramclone.utilites.EventWrapper

class LoginViewModel(
    private val authManager: AuthManager,
    private val app: Application,
    private val commonViewModel: CommonViewModel,
    onFailureListener: OnFailureListener
) : BaseViewModel(onFailureListener) {
    private val _gotoHomeScreen = MutableLiveData<EventWrapper<Unit>>() // handle problem 1 time active
    val gotoHomeScreen: LiveData<EventWrapper<Unit>> = _gotoHomeScreen

    private val _gotoRegisterScreen = MutableLiveData<EventWrapper<Unit>>()
    val gotoRegisterScreen: LiveData<EventWrapper<Unit>> = _gotoRegisterScreen

    fun onLoginClick(email: String, password: String) {
        if (validate(email, password)) {
            authManager.signIn(email, password)
                .addOnSuccessListener {
                    _gotoHomeScreen.value = EventWrapper(Unit)
                }
                .addOnFailureListener(onFailureListener)
        } else {
            commonViewModel.setErrorMessage(app.getString(R.string.please_enter_e_and_p))
        }
    }

    private fun validate(email: String, password: String) =
        email.isNotEmpty() && password.isNotEmpty()

    fun onRegisterClick() {
        _gotoRegisterScreen.value = EventWrapper(Unit)
    }
}