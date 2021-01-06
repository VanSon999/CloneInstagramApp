package vanson.dev.instagramclone.Models

class Post {
    private var postid: String = ""
    private var image: String = ""
    private var publisher: String = ""
    private var description: String = ""

    constructor()

    constructor(postid: String, image: String, publisher: String, description: String) {
        this.postid = postid
        this.image = image
        this.publisher = publisher
        this.description = description
    }

    fun getPostid(): String{
        return postid
    }

    fun getImage(): String{
        return image
    }

    fun getPublisher(): String{
        return publisher
    }

    fun getDescription(): String{
        return description
    }

    fun setPostid(postid: String){
        this.postid = postid
    }

    fun setImage(image: String){
        this.image = image
    }

    fun setPublisher(publisher: String){
        this.publisher = publisher
    }

    fun setDescription(description: String){
        this.description = description
    }
}