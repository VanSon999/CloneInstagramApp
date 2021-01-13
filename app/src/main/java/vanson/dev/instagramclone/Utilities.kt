package vanson.dev.instagramclone

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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

fun ImageView.loadUserPhoto(photoUrl: String?) {
    if (!(context as Activity).isDestroyed) {
        GlideApp.with(this).load(photoUrl).fallback(R.drawable.default_profile).into(this)
    }
}

fun Editable.toStringOrNull(): String? {
    val toString = toString()
    return if (toString.isEmpty()) null else toString
}

fun ImageView.loadImage(urlImage: String?) {
    GlideApp.with(this).load(urlImage).centerCrop().into(this)
}