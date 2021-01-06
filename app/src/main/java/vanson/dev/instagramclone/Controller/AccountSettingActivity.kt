package vanson.dev.instagramclone.Controller

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_account_setting.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R

class AccountSettingActivity : AppCompatActivity() {
    lateinit var fireBaseUser: FirebaseUser
    lateinit var fireBaseStorageImage: StorageReference
    private var changeImage: Boolean = false
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setting)

        fireBaseUser = FirebaseAuth.getInstance().currentUser!!
        fireBaseStorageImage = FirebaseStorage.getInstance().reference.child("Avatar Profile")
        userInfo()
        logout_btn_profile_frag.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        change_image_text_btn.setOnClickListener {
            changeImage = true

            CropImage.activity().setAspectRatio(1,1)
                .start(this)

        }

        save_info_profile_btn.setOnClickListener {
            if(changeImage){
                saveInfoUserAndImage()
            }else{
                saveOnlyInfoUser()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            val result = CropImage.getActivityResult(data)
            imageUri = result.uri
            profile_image_view_profile_frag.setImageURI(imageUri)
        }else{
            changeImage = false
        }
    }
    private fun saveInfoUserAndImage() {
        when {
            imageUri == null -> {
                Toast.makeText(this, "You need choose Image for Avatar!", Toast.LENGTH_LONG).show()
            }
            full_name_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "You need fill Full Name!", Toast.LENGTH_LONG).show()
            }
            username_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "You need fill User Name!", Toast.LENGTH_LONG).show()
            }
            bio_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "You need fill Bio!", Toast.LENGTH_LONG).show()
            }
            else -> {
                val progressDialog = ProgressDialog(this) //Note to it!
                progressDialog.setTitle("Update Profile")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val fileRef = fireBaseStorageImage.child(fireBaseUser.uid + ".jpg")
                var uploadTask = fileRef.putFile(imageUri!!)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                    if(!task.isSuccessful){
                        task.exception?.let { throw it }
                        progressDialog.dismiss()
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener(OnCompleteListener<Uri>{task ->
                    if(task.isSuccessful){
                        val downloadUrl = task.result
                        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(fireBaseUser.uid)
                        val userMap = HashMap<String, Any>()
                        userMap["fullname"] = full_name_profile_frag.text.toString()
                        userMap["search_name"] = full_name_profile_frag.text.toString().toLowerCase()
                        userMap["username"] = username_profile_frag.text.toString()
                        userMap["bio"] = bio_profile_frag.text.toString()
                        userMap["image"] = downloadUrl.toString()
                        usersRef.updateChildren(userMap)
                        progressDialog.dismiss()

                        Toast.makeText(this, "Your profile has been update successfully.", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }else{
                        progressDialog.dismiss()
                        Toast.makeText(this, "Error happen! Please try again!", Toast.LENGTH_LONG).show()
                    }
                })
            }
    }}

    private fun saveOnlyInfoUser() {
        when {
            full_name_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "You need fill Full Name!", Toast.LENGTH_LONG).show()
            }
            username_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "You need fill User Name!", Toast.LENGTH_LONG).show()
            }
            bio_profile_frag.text.toString() == "" -> {
                Toast.makeText(this, "You need fill Bio!", Toast.LENGTH_LONG).show()
            }
            else -> {
                val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child(fireBaseUser.uid)
                val userMap = HashMap<String, Any>()
                userMap["fullname"] = full_name_profile_frag.text.toString()
                userMap["search_name"] = full_name_profile_frag.text.toString().toLowerCase()
                userMap["username"] = username_profile_frag.text.toString()
                userMap["bio"] = bio_profile_frag.text.toString()
                usersRef.updateChildren(userMap)

                Toast.makeText(this, "Your profile has been update successfully.", Toast.LENGTH_LONG).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun userInfo(){
        val userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fireBaseUser.uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
//                if(context != null){
//                    return
//                }

                if(snapshot.exists()){
                    val user = snapshot.getValue<User>(User::class.java)
                    Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(profile_image_view_profile_frag)
                    full_name_profile_frag.setText(user.getFullName())
                    username_profile_frag.setText(user.getUsername())
                    bio_profile_frag.setText(user.getBio())
                }
            }
        })
    }
}