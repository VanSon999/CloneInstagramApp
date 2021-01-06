package vanson.dev.instagramclone.Controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import vanson.dev.instagramclone.Fragment.HomeFragment
import vanson.dev.instagramclone.Fragment.NotificationsFragment
import vanson.dev.instagramclone.Fragment.ProfileFragment
import vanson.dev.instagramclone.Fragment.SearchFragment
import vanson.dev.instagramclone.R

class MainActivity : AppCompatActivity() {
//    internal var selectedFragment : Fragment? = null
//    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.nav_home -> {
//                textView.setText("HOME")
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.nav_search -> {
//                textView.setText("SEARCH")
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.nav_add_post -> {
//                textView.setText("ADD")
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.nav_heart -> {
//                textView.setText("HEART")
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.nav_profile -> {
//                textView.setText( "PROFILE")
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//
//        false
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        nav_view.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> {
                    moveToFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_search -> {
                    moveToFragment(SearchFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_add_post -> {
                    startActivity(Intent(this, AddPostActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_heart -> {
                    moveToFragment(NotificationsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    moveToFragment(ProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

//        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
        moveToFragment(HomeFragment())
    }

    private fun moveToFragment(fragment: Fragment){
        val fragmentTrans = supportFragmentManager.beginTransaction()
        fragmentTrans.replace(R.id.fragment_container, fragment)
        fragmentTrans.commit()
    }
}