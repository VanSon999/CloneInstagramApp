package vanson.dev.instagramclone.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import vanson.dev.instagramclone.Controller.AccountSettingActivity
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.R
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var profileId: String
    private lateinit var firebaseUser: FirebaseUser
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
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val pref = context?.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if(pref != null){
            this.profileId = pref.getString("profileId", "none").toString()
        }

        if(profileId == firebaseUser.uid){
            view.edit_account_button.text = "Edit Profile"
        }else{
            checkFollowAndFollowing()
        }
        getFollower(profileId)
        getFollowings(profileId)
        userInfo()
        view.edit_account_button.setOnClickListener { view ->
            val text_edit_button = view.edit_account_button.text.toString()
            when(text_edit_button){
                "Following" -> {
                    firebaseUser.uid.let { it ->
                        FirebaseDatabase.getInstance().reference.child("Follow")
                                .child(it.toString())
                                .child("Following")
                                .child(profileId)
                                .removeValue()
                    }

                    firebaseUser.uid.let { it ->
                        FirebaseDatabase.getInstance().reference.child("Follow")
                                .child(profileId)
                                .child("Followers")
                                .child(it.toString())
                                .removeValue()
                    }
                }
                "Follow" -> {
                    firebaseUser.uid.let { it ->
                        FirebaseDatabase.getInstance().reference.child("Follow")
                                .child(it.toString())
                                .child("Following")
                                .child(profileId)
                                .setValue(true)
                    }

                    firebaseUser.uid.let { it ->
                        FirebaseDatabase.getInstance().reference.child("Follow")
                                .child(profileId)
                                .child("Followers")
                                .child(it.toString())
                                .setValue(true)
                    }
                }
                else -> {
                    startActivity(Intent(context, AccountSettingActivity::class.java))
                }
            }

        }
        return view
    }

    private fun checkFollowAndFollowing() {
        val followingRef = firebaseUser.uid.let { it ->
            FirebaseDatabase.getInstance().reference.child("Follow")
                    .child(it.toString())
                    .child("Following")
        }

        followingRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(profileId).exists()){
                    view?.edit_account_button?.text = "Following"
                }else{
                    view?.edit_account_button?.text = "Follow"
                }
            }
        })
    }

    private fun getFollower(uid: String){
//        val followersRef = firebaseUser.uid.let { it ->
//            FirebaseDatabase.getInstance().reference.child("Follow")
//                    .child(it.toString())
//                    .child("Followers")
//        }

        val followersRef = FirebaseDatabase.getInstance().reference.child("Follow")
                    .child(uid)
                    .child("Followers")

        followersRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
//                    val number = snapshot.childrenCount.toString()
//                    Log.d("Followers ", "uid : $uid : $number")
                    view?.total_followers?.text = snapshot.childrenCount.toString()
                }else{
                    view?.total_followers?.text = "0"
                }
            }
        })
    }

    private fun getFollowings(uid: String){
//        val followingsRef = firebaseUser.uid.let { it ->
//            FirebaseDatabase.getInstance().reference.child("Follow")
//                    .child(it.toString())
//                    .child("Following")
//        }
        val followingsRef = FirebaseDatabase.getInstance().reference.child("Follow")
                    .child(uid)
                    .child("Following")

        followingsRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
//                    val number = snapshot.childrenCount.toString()
//                    Log.d("Followers ", "uid : $uid : $number")
                    view?.total_following?.text = snapshot.childrenCount.toString()
                }else{
                    view?.total_following?.text = "0"
                }
            }
        })
    }

    private fun userInfo(){
        val userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(profileId)
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(context != null){ // this is problem??? context, view auto disappear after two time update profile!
                    if(snapshot.exists()){
                        val user = snapshot.getValue<User>(User::class.java)
//                    Log.d("Profile", user.toString())
                        Picasso.get().load(user!!.getImage()).placeholder(R.drawable.profile).into(view?.profile_image_fragment)
                        view?.profile_fragment_username?.text = user.getUsername()
                        view?.full_name_profile_frag?.text = user.getFullName()
                        view?.bio_profile_frag?.text  = user.getBio()
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
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

//    override fun onPause() {
//        super.onPause()
//
//        val pref = context?.getSharedPreferences("PREFS",Context.MODE_PRIVATE)?.edit()
//        pref?.putString("profileId", firebaseUser.uid)
//        pref?.apply()
//    }
//
//    override fun onStop() {
//        super.onStop()
//
//        val pref = context?.getSharedPreferences("PREFS",Context.MODE_PRIVATE)?.edit()
//        pref?.putString("profileId", firebaseUser.uid)
//        pref?.apply()
//    }

    override fun onDestroy() {
        super.onDestroy()
        val pref = context?.getSharedPreferences("PREFS",Context.MODE_PRIVATE)?.edit()
        pref?.putString("profileId", firebaseUser.uid)
        pref?.apply()
    }
}