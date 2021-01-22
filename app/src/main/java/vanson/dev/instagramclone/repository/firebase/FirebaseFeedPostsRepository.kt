package vanson.dev.instagramclone.repository.firebase

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import vanson.dev.instagramclone.controllers.home.FeedPostLikes
import vanson.dev.instagramclone.models.Comment
import vanson.dev.instagramclone.models.FeedPost
import vanson.dev.instagramclone.repository.FeedPostsRepository
import vanson.dev.instagramclone.repository.common.liveData
import vanson.dev.instagramclone.repository.common.mapCustom
import vanson.dev.instagramclone.repository.firebase.common.asComment
import vanson.dev.instagramclone.repository.firebase.common.asFeedPost
import vanson.dev.instagramclone.repository.firebase.common.database
import vanson.dev.instagramclone.repository.firebase.common.setValueTrueOrRemove
import vanson.dev.instagramclone.utilites.*

class FirebaseFeedPostsRepository : FeedPostsRepository {
    override fun createFeedPost(uid: String, feedPost: FeedPost): Task<Unit> =
        database.child("Feed-Posts").child(uid).push().setValue(feedPost).toUnit()

    override fun copyFeedPosts(postsAuthorUid: String, uid: String): Task<Unit> =
        task<Unit> { taskSource ->
            database.child("Feed-Posts").child(postsAuthorUid)
                .orderByChild("uid")
                .equalTo(postsAuthorUid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    val postsMap = it.children.map { x -> x.key to x.value }.toMap()
                    database.child("Feed-Posts").child(uid).updateChildren(postsMap)
                        .toUnit()
                        .addOnCompleteListener(TaskSourceOnCompleteListener(taskSource))
                })
        }

    override fun deleteFeedPosts(postsAuthorUid: String, uid: String): Task<Unit> =
        task<Unit> { taskSource ->
            database.child("Feed-Posts").child(uid)
                .orderByChild("uid")
                .equalTo(postsAuthorUid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {
                    val postsMap = it.children.map { x -> x.key to null }.toMap()
                    database.child("Feed-Posts").child(uid).updateChildren(postsMap)
                        .toUnit()
                        .addOnCompleteListener(TaskSourceOnCompleteListener(taskSource))
                })
        }

    override fun getFeedPosts(uid: String): LiveData<List<FeedPost>> =
        database.child("Feed-Posts").child(uid).liveData().mapCustom { dataSnapshot ->
            dataSnapshot.children.map { it.asFeedPost()!! }
        }

    override fun toggleLike(postId: String, uid: String): Task<Unit> {
        val reference = database.child("likes").child(postId).child(uid)
        return task { taskSource ->
            reference.addListenerForSingleValueEvent(ValueEventListenerAdapter {
                reference.setValueTrueOrRemove(!it.exists())
            })
            taskSource.setResult(Unit)
        }
    }

    override fun getLikes(postId: String): LiveData<List<String>> =
        database.child("likes").child(postId).liveData().mapCustom { dataSnapshot ->
            dataSnapshot.children.map { it.key!! }
        }

    override fun createComment(postId: String, comment: Comment): Task<Unit> =
        database.child("Comments").child(postId).push().setValue(comment).toUnit()

    override fun getComments(postId: String): LiveData<List<Comment>> =
        database.child("Comments").child(postId).liveData().mapCustom { dataSnapshot ->
            dataSnapshot.children.map { it.asComment()!! }
        }
}