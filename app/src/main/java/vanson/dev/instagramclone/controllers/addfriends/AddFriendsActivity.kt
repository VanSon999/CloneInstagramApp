package vanson.dev.instagramclone.controllers.addfriends

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_add_friends.*
import vanson.dev.instagramclone.adapters.FriendsAdapter
import vanson.dev.instagramclone.models.User
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.BaseActivity
import vanson.dev.instagramclone.controllers.common.setupAuthGuard

class AddFriendsActivity : BaseActivity(), FriendsAdapter.Listener {

    private lateinit var mViewModel: AddFriendsViewModel

    //    private val tag = "AddFriendsActivity"
    private lateinit var mAdapter: FriendsAdapter
    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)
        mAdapter = FriendsAdapter(this)

        setupAuthGuard {
            mViewModel = initViewModel()

            back_image.setOnClickListener { finish() }
            add_friends_recycler.adapter = mAdapter
            add_friends_recycler.layoutManager = LinearLayoutManager(this)

            mViewModel.userAndFriends.observe(this, Observer {
                it?.let { (user, otherUsers) -> // check if value of _userAndFriend != null
                    mUser = user
                    mAdapter.update(otherUsers, mUser.Follows)
                }
            })
        }
    }

    override fun follow(uid: String) {
        setFollow(uid, true) {
            mAdapter.followed(uid)
        }
    }

    override fun unfollow(uid: String) {
        setFollow(uid, false) {
            mAdapter.unfollowed(uid)
        }
    }

    private fun setFollow(uid: String, follow: Boolean, onSuccess: () -> Unit) {
        mViewModel.setFollow(mUser.uid, uid, follow).addOnSuccessListener {
            onSuccess()
        }
    }
}

