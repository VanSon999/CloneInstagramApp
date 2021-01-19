package vanson.dev.instagramclone.controllers.common

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import vanson.dev.instagramclone.R

fun Context.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    text?.let {Toast.makeText(this, it, duration).show()}
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
