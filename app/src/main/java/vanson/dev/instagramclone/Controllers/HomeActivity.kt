package vanson.dev.instagramclone.Controllers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.acitivity_home.*
import vanson.dev.instagramclone.R

class HomeActivity : BaseActivity(0) {
    private val TAG = "HomeActivity"
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_home)
        setupBottomNavigation()
        Log.d(TAG, "onCreate: ${this.navNumber}")

        mAuth = FirebaseAuth.getInstance()

        sign_out_text.setOnClickListener {
            mAuth.signOut()
        }
        mAuth.addAuthStateListener {
            if(mAuth.currentUser == null){
                startActivity(Intent(this,
                    LoginActivity::class.java))
                finish()
            }
        }
//        mAuth.signOut()
//        mAuth.signInWithEmailAndPassword("thorjim99@gmail.com", "01213512168")
//            .addOnCompleteListener {
//                if(it.isSuccessful){
//                    Log.d(TAG, "sign in: success")
//                }else{
//                    Log.d(TAG, "sign in: failure")
//                }
//            }
    }

    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}