package vanson.dev.instagramclone.controllers.register

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.home.HomeActivity

class RegisterActivity : BaseActivity(), EmailFragment.Listener, NamePassFragment.Listener {
    private lateinit var mViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mViewModel = initViewModel()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.frame_layout, EmailFragment())
                .commit()
        }
        mViewModel.gotoNamePassScreen.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, NamePassFragment())
                    .addToBackStack(null) //save state for user back to EmailFragment
                    .commit()
            }
        })

        mViewModel.gotoHomeScreen.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                startHomeActivity()
            }
        })

        mViewModel.goBackToEmailScreen.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                supportFragmentManager.popBackStack()
            }
        })
    }

    override fun onNext(email: String) {
        mViewModel.onEmailEntered(email)
    }


    override fun onRegister(fullname: String, password: String) {
        mViewModel.onRegister(fullname, password)

    }

    private fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }


    companion object {
        private val tag = "RegisterActivity"
    }
}

//private fun unknowRegisterError(it: Task<*>) {
//    Log.e(tag, "onRegister: fail to create user profile", it.exception)
//    showToast(getString(R.string.something_wrong_try_again_later))
//}
//private fun FirebaseAuth.fetchSignInMethodsForEmail(
//    email: String,
//    onSuccess: (List<String>) -> Unit
//) {
//    fetchSignInMethodsForEmail(email).addOnCompleteListener {
//        if (it.isSuccessful) {
//            onSuccess(it.result?.signInMethods ?: emptyList()) //elvis operator
//        } else {
//            showToast(it.exception!!.message!!)
//        }
//    }
//}
//private fun FirebaseAuth.createUserWithEmailAndPassword(
//    email: String,
//    password: String,
//    onSuccess: (AuthResult) -> Unit
//) {
//    createUserWithEmailAndPassword(email, password).addOnCompleteListener {
//        if (it.isSuccessful) {
//            onSuccess(it.result!!)
//        } else {
//            unknowRegisterError(it)
//        }
//    }
//}
//
//private fun DatabaseReference.createUser(uid: String, user: User, onSuccess: () -> Unit) {
//    child("Users").child(uid).setValue(user).addOnCompleteListener {
//        if (it.isSuccessful) {
//            onSuccess()
//        } else {
//            unknowRegisterError(it)
//        }
//    }
//}
