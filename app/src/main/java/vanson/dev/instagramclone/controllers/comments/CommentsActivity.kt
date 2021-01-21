package vanson.dev.instagramclone.controllers.comments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_comments.*
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.adapters.CommentsAdapter
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.common.setupAuthGuard
import vanson.dev.instagramclone.models.User

class CommentsActivity : BaseActivity() {
    private lateinit var mUser: User
    private lateinit var mAdapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        back_image.setOnClickListener { finish() }
        val postId = intent.getStringExtra(EXTRA_POST_ID) ?: return finish()
        setupAuthGuard {
            comments_recycle.layoutManager = LinearLayoutManager(this)
            mAdapter = CommentsAdapter()
            comments_recycle.adapter = mAdapter

            val viewModel = initViewModel<CommentsViewModel>()
            viewModel.user.observe(this, Observer {
                it?.let {
                    mUser = it
                }
            })
            viewModel.init(postId)
            viewModel.comments.observe(this, Observer {
                it?.let {
                    mAdapter.updateComments(it)
                }
            })

            post_comment_text.setOnClickListener {
                viewModel.createComment(comment_text.text.toString(), mUser)
                comment_text.setText("")
            }
        }
    }

    companion object {
        private const val EXTRA_POST_ID = "POST_ID"

        fun start(context: Context, postId: String) {
            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra(EXTRA_POST_ID, postId)
            context.startActivity(intent)
        }
    }
}