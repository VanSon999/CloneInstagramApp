package vanson.dev.instagramclone.controllers.home

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.acitivity_home.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.adapters.FeedAdapter
import vanson.dev.instagramclone.controllers.comments.CommentsActivity
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.common.setupAuthGuard
import vanson.dev.instagramclone.views.setupBottomNavigation

class HomeActivity : BaseActivity(), FeedAdapter.Listener {
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mAdapter: FeedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_home)
        setupBottomNavigation(0)

        mAdapter = FeedAdapter(this)
        recycler_posts.adapter = mAdapter
        recycler_posts.layoutManager = LinearLayoutManager(this)

        setupAuthGuard { uid ->
            mViewModel = initViewModel()
            mViewModel.init(uid)
            mViewModel.feedPosts.observe(this, Observer {
                it?.let {
                    mAdapter.updatePosts(it)
                }
            })

            mViewModel.gotoCommentScreen.observe(this , Observer { event_it ->
                event_it.getContentIfNotHandled()?.let{
                     CommentsActivity.start(this ,it)
                }
            })
        }
    }

    override fun toggleLike(postId: String) {
        mViewModel.toggleLike(postId)
    }

    override fun loadLikes(postId: String, position: Int) {
        if (mViewModel.getLikes(postId) == null) {
            mViewModel.loadLikes(postId).observe(this, Observer {
                it?.let {
                    mAdapter.updatePostLikes(position, it)
                }
            })
        }
    }

    override fun openComments(id: String) {
        mViewModel.openComments(id)
    }

    companion object {
        private val tag = "HomeActivity"
    }
}

