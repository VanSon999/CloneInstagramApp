package vanson.dev.instagramclone.Models

class User {
    private var username: String = ""
    private var fullname: String = ""
    private var bio: String = ""
    private var image: String = ""
    private var uid: String = ""

    constructor()

    constructor(username: String, fullname: String, bio: String, image: String, uid: String){
        this.username = username
        this.fullname = fullname
        this.bio = bio
        this.image = image
        this.uid = uid
    }

    fun getUsername(): String {
        return username
    }

    fun setUsername(username: String): Unit {
        this.username = username
    }

    fun getFullName(): String {
        return fullname
    }

    fun setFullName(fullname: String): Unit {
        this.fullname = fullname
    }

    fun getBio(): String {
        return bio
    }

    fun setBio(bio: String): Unit {
        this.bio = bio
    }

    fun getImage(): String {
        return image
    }

    fun setImage(image: String): Unit {
        this.image = image
    }

    fun getUid(): String {
        return uid
    }

    fun setUid(uid: String): Unit {
        this.uid = uid
    }

    override fun toString(): String {
        return "FullName: ${this.fullname}, UserName: ${this.username}, Bio: ${this.bio}, uid: ${this.uid}"
    }
}