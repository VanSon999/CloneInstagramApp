package vanson.dev.instagramclone.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.view.*
import vanson.dev.instagramclone.Adapters.UserAdapter
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private var userAdapter: UserAdapter? = null
    private lateinit var mUser: ArrayList<User>
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
        val view =  inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_search)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        //for fragment, context can null, so you have to use context.let to check it
        mUser = ArrayList()
        userAdapter = context?.let { UserAdapter(it, mUser, true) }
        recyclerView.adapter = userAdapter

        view.search_edit_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Test", "Change")
                if(view.search_edit_text.text.toString() != ""){
                    Log.d("Test", "Text : ${p0.toString()}")
                    recyclerView.visibility = View.VISIBLE
                    retrieveUsers()
                    searchUser(p0.toString().toLowerCase())
                }
            }
        })
        return view
    }

    private fun searchUser(str: String) {
        val query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("search_name").startAt(str).endAt(str + "\uf8ff")

        query.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mUser.clear()

                for(x in snapshot.children){
                    val user = x.getValue(User::class.java)
                    Log.d("Test", "Search -- " + user)
                    if(user != null){
                        mUser.add(user)
                    }
                }

                userAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun retrieveUsers() {
        val userRef = FirebaseDatabase.getInstance().getReference().child("Users")
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(x in snapshot.children){
                    val temp = x.getValue(User::class.java)
                    Log.d("Test", "Retrieve -- " + temp)
                }
                if(view?.search_edit_text?.text.toString() == ""){
                    mUser.clear()
                    for(x in snapshot.children){
                        val user = x.getValue(User::class.java)
                        if(user != null){
                            mUser.add(user)
                        }
                    }
                    userAdapter?.notifyDataSetChanged()
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
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}