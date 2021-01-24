package vanson.dev.instagramclone.controllers.register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.BaseViewModel
import vanson.dev.instagramclone.controllers.common.CommonViewModel
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.repository.UsersRepository
import vanson.dev.instagramclone.utilites.EventWrapper
import java.util.*

class RegisterViewModel(
    private val app: Application,
    private val usersRepo: UsersRepository,
    private val commonViewModel: CommonViewModel,
    onFailureListener: OnFailureListener
) : BaseViewModel(onFailureListener) {
    private var email: String? = null
    private val _gotoNamePassScreen = MutableLiveData<EventWrapper<Unit>>()
    val gotoNamePassScreen = _gotoNamePassScreen

    private val _gotoHomeScreen =  MutableLiveData<EventWrapper<Unit>>()
    val gotoHomeScreen = _gotoHomeScreen

    private val _goBackToEmailScreen =  MutableLiveData<EventWrapper<Unit>>()
    val goBackToEmailScreen = _goBackToEmailScreen

    fun onEmailEntered(email: String) {
        if (email.isNotEmpty()) {
            this.email = email
            usersRepo.isUserExistsForEmail(email).addOnSuccessListener { exists ->
                if (!exists) {
                    _gotoNamePassScreen.value = EventWrapper(Unit)
                } else {
                    commonViewModel.setErrorMessage(app.getString(R.string.email_already_exists))
                }
            }.addOnFailureListener(onFailureListener)
        } else {
            commonViewModel.setErrorMessage(app.getString(R.string.please_enter_email))
        }
    }

    fun onRegister(fullName: String, password: String) {
        if (fullName.isNotEmpty() && password.isNotEmpty()) {
            val localEmail = email
            if (localEmail != null) { //check again because mEmail can be change with var!
                usersRepo.createUser(mUser(fullName, localEmail), password).addOnSuccessListener {
                    _gotoHomeScreen.value = EventWrapper(Unit)
                }.addOnFailureListener(onFailureListener)
            } else {
                commonViewModel.setErrorMessage(app.getString(R.string.please_enter_email))
                _goBackToEmailScreen.value = EventWrapper(Unit) // back to EmailFragment
            }
        } else {
            commonViewModel.setErrorMessage(app.getString(R.string.please_enter_fullname_pass))
        }
    }

    private fun mUser(fullName: String, email: String): User {
        val username = mUserName(fullName)
        return User(name = fullName, username = username, email = email)
    }

    private fun mUserName(fullname: String) = fullname.toLowerCase(Locale.ROOT).replace(" ", ".")
}
