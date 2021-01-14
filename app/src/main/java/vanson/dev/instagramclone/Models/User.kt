package vanson.dev.instagramclone.Models

import com.google.firebase.database.Exclude

data class User(
    val name: String = "",
    val username: String = "",
    val website: String? = null,
    val bio: String? = null,
    val email: String = "",
    val phone: String? = null,
    val photo: String? = null,
    val Follows: Map<String, Boolean> = emptyMap(),
    val Followers: Map<String, Boolean> = emptyMap(),
    @Exclude val uid: String = "" //@Exclude to show that uid will be export from database
)