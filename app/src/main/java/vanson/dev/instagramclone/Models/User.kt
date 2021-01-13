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
    val follows: Map<String, Boolean> = emptyMap(),
    val followers: Map<String, Boolean> = emptyMap(),
    @Exclude val uid: String? = null //@Exclude to show that uid will be export from database
)