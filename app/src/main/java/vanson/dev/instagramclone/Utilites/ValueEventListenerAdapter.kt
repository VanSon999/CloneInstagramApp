package vanson.dev.instagramclone.Utilites

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ValueEventListenerAdapter(val handler: (DataSnapshot) -> Unit) :
    ValueEventListener {
    private val TAG = "ValueEventListenerAdapt"
    override fun onCancelled(error: DatabaseError) {
        Log.d(TAG, "onCancelled: ", error.toException())
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        handler(snapshot)
    }
}