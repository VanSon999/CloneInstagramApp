package vanson.dev.instagramclone.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import vanson.dev.instagramclone.Adapters.PostAdapter
import vanson.dev.instagramclone.Models.Post
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [home_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private var postAdapter : PostAdapter? = null
    private var postList: ArrayList<Post>? = null
    private var followingList: ArrayList<String>? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        var recyclerView: RecyclerView? = null
        recyclerView = view.findViewById(R.id.recycler_view_home)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = linearLayoutManager

        postList = ArrayList()
        followingList = ArrayList()
        postAdapter = context?.let { PostAdapter(postList!!, it) }

        checkFollowings()
        return view
    }

    private fun checkFollowings() {
        val followingRef = FirebaseDatabase.getInstance().reference.child("Follow").child(
            FirebaseAuth.getInstance().currentUser!!.uid).child("Following")

        followingRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                followingList?.clear()
                if(snapshot.exists()){
                    for(x in snapshot.children){
                        x.key?.let { followingList!!.add(it) }
                    }
                }
                retrievePosts()
            }
        })

    }

    private fun retrievePosts() {
        val postRef = FirebaseDatabase.getInstance().reference.child("Posts")

        postRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                postList?.clear()
                if(snapshot.exists()){
                    for(x in snapshot.children){
                        val post = x.getValue<Post>(Post::class.java)
                        for(id in followingList!!){
                            if(post!!.getPublisher() == id){
                                postList!!.add(post)
                            }
                        }
                        postAdapter!!.notifyDataSetChanged()
                    }
                }
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}