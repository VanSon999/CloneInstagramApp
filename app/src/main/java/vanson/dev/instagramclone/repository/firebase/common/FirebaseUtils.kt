package vanson.dev.instagramclone.repository.firebase.common

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import vanson.dev.instagramclone.models.Comment
import vanson.dev.instagramclone.models.FeedPost
import vanson.dev.instagramclone.models.User

//fun DatabaseReference.setValueTrueOrRemove(follow: Boolean) =
//    if (follow) setValue(true) else removeValue()

val database: DatabaseReference = FirebaseDatabase.getInstance().reference
val storage: StorageReference = FirebaseStorage.getInstance().reference
val auth: FirebaseAuth = FirebaseAuth.getInstance()