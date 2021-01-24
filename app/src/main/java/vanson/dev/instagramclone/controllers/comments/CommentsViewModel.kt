package vanson.dev.instagramclone.controllers.comments

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.controllers.common.BaseViewModel
import vanson.dev.instagramclone.models.Comment
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.repository.FeedPostsRepository
import vanson.dev.instagramclone.repository.UsersRepository

class CommentsViewModel(private val feedPostsRepo: FeedPostsRepository,
                        private val usersRepo: UsersRepository,
                        onFailureListener: OnFailureListener) : BaseViewModel(onFailureListener) {
    private lateinit var postId: String
    val user: LiveData<User> = usersRepo.getUser()
    lateinit var comments: LiveData<List<Comment>>

    fun init(postId: String) {
        this.postId = postId
        comments = feedPostsRepo.getComments(postId)
    }

    fun createComment(text: String, user: User) {
        val comment = Comment(
            uid = user.uid,
            username = user.username,
            photo = user.photo,
            text = text)

        feedPostsRepo.createComment(postId, comment).addOnFailureListener(onFailureListener)
    }


}
