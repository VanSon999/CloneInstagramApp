package vanson.dev.instagramclone

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import vanson.dev.instagramclone.Fragment.HomeFragment
import vanson.dev.instagramclone.Fragment.NotificationsFragment
import vanson.dev.instagramclone.Fragment.ProfileFragment
import vanson.dev.instagramclone.Fragment.SearchFragment

class MainActivity : AppCompatActivity() {
    internal var selectedFragment : Fragment? = null
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

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> {
                    selectedFragment = HomeFragment()
                    true
                }
                R.id.nav_search -> {
                    selectedFragment = SearchFragment()
                    true
                }
                R.id.nav_add_post -> {

                    true
                }
                R.id.nav_heart -> {
                    selectedFragment = NotificationsFragment()
                    true
                }
                R.id.nav_profile -> {
                    selectedFragment = ProfileFragment()
                    true
                }
            }
            if(selectedFragment != null){
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    selectedFragment!!
                ).commit()
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
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            HomeFragment()
        ).commit()
    }
}