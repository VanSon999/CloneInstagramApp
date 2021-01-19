package vanson.dev.instagramclone.controllers

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import kotlinx.android.synthetic.main.activity_login.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.CommonViewModel
import vanson.dev.instagramclone.controllers.home.HomeActivity
import vanson.dev.instagramclone.utilites.AuthManager

class LoginViewModel(
    private val authManager: AuthManager,
    private val app: Application,
    private val commonViewModel: CommonViewModel,
    private val onFailureListener: OnFailureListener
) : ViewModel() {
    private val _gotoHomeScreen = MutableLiveData<Unit>()
    val gotoHomeScreen: LiveData<Unit> = _gotoHomeScreen

    private val _gotoRegisterScreen = MutableLiveData<Unit>()
    val gotoRegisterScreen: LiveData<Unit> = _gotoRegisterScreen

    fun onLoginClick(email: String, password: String) {
        if (validate(email, password)) {
            authManager.signIn(email, password)
                .addOnSuccessListener {
                    _gotoHomeScreen.value = Unit
                }
                .addOnFailureListener(onFailureListener)
        } else {
            commonViewModel.setErrorMessage(app.getString(R.string.please_enter_e_and_p))
        }
    }

    private fun validate(email: String, password: String) =
        email.isNotEmpty() && password.isNotEmpty()

    fun onRegisterClick() {
        _gotoRegisterScreen.value = Unit
    }
}