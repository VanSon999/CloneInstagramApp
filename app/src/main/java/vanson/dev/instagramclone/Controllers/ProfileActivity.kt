package vanson.dev.instagramclone.Controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_profile.*
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.Utilites.FirebaseHelper
import vanson.dev.instagramclone.Utilites.GlideApp
import vanson.dev.instagramclone.Utilites.ValueEventListenerAdapter
import vanson.dev.instagramclone.asUser
import vanson.dev.instagramclone.loadImage
import vanson.dev.instagramclone.loadUserPhoto

class ProfileActivity : BaseActivity(4) {
    private lateinit var mUser: User
    private lateinit var mFirebase: FirebaseHelper
    private val TAG = "ProfileActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation()
        Log.d(TAG, "onCreate: ${this.navNumber}")

        edit_profile_btn.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        add_friends_image.setOnClickListener {
            val intent = Intent(this, AddFriendsActivity::class.java)
            startActivity(intent)
        }
        settings_image.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }
        mFirebase = FirebaseHelper(this)
        mFirebase.currentUserReference().addValueEventListener(ValueEventListenerAdapter {
            mUser = it.asUser()!!
            profile_image.loadUserPhoto(mUser.photo)
            username_text.text = mUser.username
        })

        images_recycler.layoutManager = GridLayoutManager(this, 3)
        mFirebase.database.child("images").child(mFirebase.auth.currentUser!!.uid).addValueEventListener(ValueEventListenerAdapter{
            val images = it.children.map {it.getValue(String:: class.java)!!}
            images_recycler.adapter = ImagesAdapter(images)
        })
    }
}

class ImagesAdapter(private val images: List<String>) :
    RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    class ViewHolder(val image: ImageView) : RecyclerView.ViewHolder(image)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item, parent, false) as ImageView
        return ViewHolder(imageView)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.loadImage(images[position])
    }

//    private fun ImageView.loadImage(urlImage: String){
//        GlideApp.with(this).load(urlImage).centerCrop().into(this)
//    }
}

class SquareImageView(context: Context, attrs: AttributeSet) : androidx.appcompat.widget.AppCompatImageView(context, attrs){
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) { //to fix size of image View
        super.onMeasure(widthMeasureSpec, widthMeasureSpec) // -> square
    }
}