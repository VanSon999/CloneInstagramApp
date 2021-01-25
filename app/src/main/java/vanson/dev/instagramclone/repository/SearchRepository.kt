package vanson.dev.instagramclone.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import vanson.dev.instagramclone.models.SearchPost

interface SearchRepository {
    fun searchPosts(text: String): LiveData<List<SearchPost>>
    fun createPost(post: SearchPost): Task<Unit>
}