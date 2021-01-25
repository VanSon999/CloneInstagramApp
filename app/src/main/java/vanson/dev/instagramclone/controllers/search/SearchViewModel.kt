package vanson.dev.instagramclone.controllers.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.controllers.common.BaseViewModel
import vanson.dev.instagramclone.models.FeedPost
import vanson.dev.instagramclone.models.SearchPost
import vanson.dev.instagramclone.repository.SearchRepository

class SearchViewModel(searchRepo: SearchRepository, onFailureListener: OnFailureListener) : BaseViewModel(onFailureListener){

    private val searchText = MutableLiveData<String>()
    val posts: LiveData<List<SearchPost>> =  Transformations.switchMap(searchText) { text ->
        searchRepo.searchPosts(text)
    }

    fun setSearchText(text: String) {
        searchText.value = text
    }
}