package vanson.dev.instagramclone

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.DataSnapshot
import vanson.dev.instagramclone.Models.User
import vanson.dev.instagramclone.Utilites.GlideApp

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


fun Editable.toStringOrNull(): String? {
    val toString = toString()
    return if (toString.isEmpty()) null else toString
}

fun ImageView.loadUserPhoto(photoUrl: String?) =
    ifNotDestroyed {
        GlideApp.with(this).load(photoUrl).fallback(R.drawable.default_profile).into(this)
    }

fun ImageView.loadImage(urlImage: String?) =
    ifNotDestroyed { GlideApp.with(this).load(urlImage).centerCrop().into(this) }


private fun View.ifNotDestroyed(block: () -> Unit) {
    if (!(context as Activity).isDestroyed) {
        block()
    }
}

fun <T> task(block: (TaskCompletionSource<T>) -> Unit): Task<T> { //this is it when addOnCompleteListener!!!
    val taskSource = TaskCompletionSource<T>()
    block(taskSource)
    return taskSource.task // this is equal mFirebase.database.child("Feed_Posts").child(mUser.uid!!).updateChildren(postsMap).addOnCompleteListener ...
}

fun DataSnapshot.asUser(): User? = getValue(User::class.java)?.copy(uid = key.toString())