package vanson.dev.instagramclone.repository.firebase

import com.google.android.gms.tasks.Task
import vanson.dev.instagramclone.repository.FeedPostsRepository
import vanson.dev.instagramclone.repository.firebase.common.database
import vanson.dev.instagramclone.utilites.*

class FirebaseFeedPostsRepository : FeedPostsRepository {
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
}