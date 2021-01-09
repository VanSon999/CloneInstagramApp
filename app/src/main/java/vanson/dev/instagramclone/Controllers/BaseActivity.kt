package vanson.dev.instagramclone.Controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.bottom_navigation_view.*
import vanson.dev.instagramclone.*

abstract class BaseActivity(val navNumber: Int) : AppCompatActivity(){
    private val TAG = "BaseActivity"

    fun setupBottomNavigation(){
        bottom_navigation_view.setTextVisibility(false)
        bottom_navigation_view.enableItemShiftingMode(false)
        bottom_navigation_view.enableShiftingMode(false)
        bottom_navigation_view.enableAnimation(false)
        for(x in 0 until bottom_navigation_view.menu.size()){
            bottom_navigation_view.setIconTintList(x, null)
        }

        bottom_navigation_view.setOnNavigationItemSelectedListener {
            val nextActivity = when(it.itemId){
                R.id.nav_ic_home -> HomeActivity::class.java
                R.id.nav_ic_likes -> LikesActivity::class.java
                R.id.nav_ic_search -> SearchActivity::class.java
                R.id.nav_ic_share -> ShareActivity::class.java
                R.id.nav_ic_profile -> ProfileActivity::class.java
                else ->{
                    null
                }
            }
            if(nextActivity!=null){
                val intent = Intent(this, nextActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                overridePendingTransition(0,0) //turn of animation convert Activity
                startActivity(intent)
                true
            }else{
                false
            }
        }

        bottom_navigation_view.menu.getItem(navNumber).isChecked = true
    }

    fun setActivityChecked(num: Int): Unit{
        bottom_navigation_view.menu.getItem(num).isChecked = true
    }
}