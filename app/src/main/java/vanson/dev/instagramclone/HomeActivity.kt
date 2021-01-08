package vanson.dev.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class HomeActivity : BaseActivity(0) {
    private val TAG = "HomeActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_home)
        setupBottomNavigation()
        Log.d(TAG, "onCreate: ${this.navNumber}")
    }

    override fun onStart() {
        super.onStart()
        setActivityChecked(0)
    }
}