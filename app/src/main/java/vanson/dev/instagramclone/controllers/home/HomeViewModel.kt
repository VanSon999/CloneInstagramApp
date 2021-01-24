package vanson.dev.instagramclone.controllers.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import vanson.dev.instagramclone.controllers.common.BaseViewModel
import vanson.dev.instagramclone.models.FeedPost
import vanson.dev.instagramclone.repository.FeedPostsRepository
import vanson.dev.instagramclone.repository.common.mapCustom
import vanson.dev.instagramclone.utilites.EventWrapper

class HomeViewModel(
    onFailureListener: OnFailureListener,
    private val feedPostsRepo: FeedPostsRepository
) : BaseViewModel(onFailureListener) {
    private val _gotoCommentScreen = MutableLiveData<EventWrapper<String>>()
    val gotoCommentScreen = _gotoCommentScreen
    lateinit var uid: String
    lateinit var feedPosts: LiveData<List<FeedPost>>
    private var loadedLikes = mapOf<String, LiveData<FeedPostLikes>>()

    fun init(uid: String) {
        this.uid = uid
        feedPosts = feedPostsRepo.getFeedPosts(uid)
            .mapCustom { it -> it.sortedByDescending { it.timestampDate() } }
    }

    fun toggleLike(postId: String) {
        feedPostsRepo.toggleLike(postId, uid).addOnFailureListener(onFailureListener)
    }

    fun getLikes(postId: String): LiveData<FeedPostLikes>? = loadedLikes[postId]

    fun loadLikes(postId: String): LiveData<FeedPostLikes> {
        val liveDataAvailable = loadedLikes[postId]
        return if (liveDataAvailable == null) {
            val liveData = feedPostsRepo.getLikes(postId).mapCustom { likes ->
                FeedPostLikes(
                    likesCount = likes.size,
                    likeByUser = likes.find { it == uid } != null)
            }
            loadedLikes = loadedLikes + (postId to liveData)
            liveData
        } else {
            liveDataAvailable
        }
    }

    fun openComments(postId: String) {
        _gotoCommentScreen.value = EventWrapper(postId)
    }

}