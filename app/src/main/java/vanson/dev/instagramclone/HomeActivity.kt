package vanson.dev.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : BaseActivity(0) {
    private val TAG = "HomeActivity"
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_home)
        setupBottomNavigation()
        Log.d(TAG, "onCreate: ${this.navNumber}")

        mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
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
        setActivityChecked(0)
        if(mAuth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}