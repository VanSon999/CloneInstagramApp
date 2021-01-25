package vanson.dev.instagramclone.repository.firebase

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import vanson.dev.instagramclone.models.SearchPost
import vanson.dev.instagramclone.repository.SearchRepository
import vanson.dev.instagramclone.repository.common.FirebaseLiveData
import vanson.dev.instagramclone.repository.common.liveData
import vanson.dev.instagramclone.repository.common.mapCustom
import vanson.dev.instagramclone.repository.firebase.common.database
import vanson.dev.instagramclone.utilites.toUnit
import java.util.*

class FirebaseSearchRepository : SearchRepository {
    override fun createPost(post: SearchPost): Task<Unit> =
        database.child("Search-Posts").push()
            .setValue(post.copy(caption = post.caption.toLowerCase(Locale.ROOT))).toUnit()

    override fun searchPosts(text: String): LiveData<List<SearchPost>> {
        val reference = database.child("Search-Posts")
        val query = if (text.isEmpty()) {
            reference
        } else {
            reference.orderByChild("caption").startAt(text.toLowerCase(Locale.ROOT)).endAt(
                "${
                    text.toLowerCase(
                        Locale.ROOT
                    )
                }\\uf8ff"
            )
        }
        return FirebaseLiveData(query).mapCustom { dataSnapshot ->
            dataSnapshot.children.map { it.asSearchPost()!! }
        }
    }
}

private fun DataSnapshot.asSearchPost(): SearchPost? =
    getValue(SearchPost::class.java)?.copy(id = key.toString())