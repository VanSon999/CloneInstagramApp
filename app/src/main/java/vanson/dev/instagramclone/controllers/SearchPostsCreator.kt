package vanson.dev.instagramclone.controllers

import android.util.Log
import androidx.lifecycle.Observer
import vanson.dev.instagramclone.models.SearchPost
import vanson.dev.instagramclone.repository.SearchRepository
import vanson.dev.instagramclone.utilites.BaseEventListener
import vanson.dev.instagramclone.utilites.Event
import vanson.dev.instagramclone.utilites.EventBus

class SearchPostsCreator(searchRepo: SearchRepository) : BaseEventListener() {
    init {
        EventBus.events.observe(this, Observer {
            it?.let { event ->
                when (event) {
                    is Event.CreateSearchPost -> {
                        with(event.feedPost) {
                            val searchPost = SearchPost(
                                image = image,
                                caption = caption,
                                postId = id
                            )
                            searchRepo.createPost(searchPost).addOnFailureListener {
                                Log.d(TAG, "Failed to create search post for event: $event", it)
                            }
                        }
                    }
                    else -> {
                    }
                }
            }
        })
    }

    companion object{
        const val TAG = "SearchPostsCreator"
    }
}