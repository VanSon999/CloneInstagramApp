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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_account_setting.*
import kotlinx.android.synthetic.main.activity_add_post.*
import vanson.dev.instagramclone.R

class AddPostActivity : AppCompatActivity() {
    private var imageUri: Uri? = null
    lateinit var fireBaseStorageImage: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        fireBaseStorageImage = FirebaseStorage.getInstance().reference.child("Post Pictures")

        save_new_post_btn.setOnClickListener { uploadImage() }

        CropImage.activity().setAspectRatio(2,1).start(this)
    }

    private fun uploadImage() {
        when{
            imageUri == null -> Toast.makeText(this, "You need choose Image!", Toast.LENGTH_LONG).show()
            description_post.text.toString() == "" -> Toast.makeText(this, "You need fill description!", Toast.LENGTH_LONG).show()
            else -> {
                var progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Adding New Post")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val fileRef = fireBaseStorageImage.child(System.currentTimeMillis().toString() + ".jpg")

                var uploadTask = fileRef.putFile(imageUri!!)
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{task ->
                    if(!task.isSuccessful){
                        task.exception?.let{
                            throw it
                            progressDialog.dismiss()
                        }
                    }
                    return@Continuation fileRef.downloadUrl
                }).addOnCompleteListener(OnCompleteListener<Uri>{task ->
                    if(task.isSuccessful){
                        val downloadUrl = task.result
                        val ref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Posts")
                        val postId = ref.push().key!!
                        val postMap = HashMap<String, Any>()
                        postMap["postid"] = postId
                        postMap["description"] = description_post.text.toString()
                        postMap["publisher"] = FirebaseAuth.getInstance().currentUser!!.uid
                        postMap["image"] = downloadUrl.toString()
                        ref.child(postId).setValue(postMap)
                        progressDialog.dismiss()

                        Toast.makeText(this, "Post upload successfully.", Toast.LENGTH_LONG).show()
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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            var result = CropImage.getActivityResult(data)
            imageUri = result.uri
            image_post.setImageURI(imageUri)
        }
    }
}