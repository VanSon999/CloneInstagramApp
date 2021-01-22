package vanson.dev.instagramclone.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import vanson.dev.instagramclone.controllers.home.FeedPostLikes
import vanson.dev.instagramclone.models.Comment
import vanson.dev.instagramclone.models.FeedPost

interface FeedPostsRepository {
    fun copyFeedPosts(postsAuthorUid: String, uid: String): Task<Unit>
    fun deleteFeedPosts(postsAuthorUid: String, uid: String): Task<Unit>
    fun getFeedPosts(uid: String): LiveData<List<FeedPost>>
    fun toggleLike(postId: String, uid: String): Task<Unit>
    fun getLikes(postId: String): LiveData<List<String>> //String is userId
    fun getComments(postId: String): LiveData<List<Comment>>
    fun createComment(postId: String, comment: Comment): Task<Unit>
    fun createFeedPost(uid: String, feedPost: FeedPost): Task<Unit>
}