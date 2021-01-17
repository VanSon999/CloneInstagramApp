package vanson.dev.instagramclone.Controllers.addfriends

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_add_friends.*
import vanson.dev.instagramclone.Adapters.FriendsAdapter
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R

class AddFriendsActivity : AppCompatActivity(), FriendsAdapter.Listener {

    private lateinit var mViewModel: AddFriendsViewModel

    //    private val tag = "AddFriendsActivity"
    private lateinit var mAdapter: FriendsAdapter
    private lateinit var mUsers: List<User>
    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)
        mAdapter = FriendsAdapter(this)

        mViewModel = ViewModelProvider(this, AddFriendsViewModelFactory()).get(AddFriendsViewModel::class.java)

        back_image.setOnClickListener { finish() }
        add_friends_recycler.adapter = mAdapter
        add_friends_recycler.layoutManager = LinearLayoutManager(this)

        mViewModel.userAndFriends.observe(this, Observer {
            it?.let { (user, otherUsers) -> // check if value of _userAndFriend != null
                mUser = user
                mUsers = otherUsers

                mAdapter.update(mUsers, mUser.Follows)
            }
        })
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
        }.addOnFailureListener { it.message }
    }
}

