package vanson.dev.instagramclone

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ValueEventListenerAdapter(val handler: (DataSnapshot) -> Unit) : ValueEventListener {
    private val TAG = "ValueEventListenerAdapt"
    override fun onCancelled(error: DatabaseError) {
        Log.d(TAG, "onCancelled: ", error.toException())
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        handler(snapshot)
    }
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
} //Context is abstract, it will be come context of activity that it be called

fun determineStateBtn(btn: Button, vararg input: EditText) {
    val watcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            btn.isEnabled = input.all { it.text.isNotEmpty() }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    }
    input.forEach {
        it.addTextChangedListener(watcher)
    }
    btn.isEnabled = input.all { it.text.isNotEmpty() }
}