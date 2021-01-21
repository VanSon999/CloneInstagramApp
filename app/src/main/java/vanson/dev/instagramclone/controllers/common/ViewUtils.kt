package vanson.dev.instagramclone.controllers.common

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.utilites.formatRelativeTimestamp
import java.util.*

fun Context.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    text?.let { Toast.makeText(this, it, duration).show() }
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

fun TextView.setCaptionText(username: String, caption: String, date: Date? = null) {
    val usernameSpannable = SpannableString(username)
    usernameSpannable.setSpan(
        StyleSpan(Typeface.BOLD),
        0,
        usernameSpannable.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    usernameSpannable.setSpan(object : ClickableSpan() {
        override fun onClick(p0: View) {
            p0.context.showToast("Username is clicked")
        }

        override fun updateDrawState(ds: TextPaint) {}
    }, 0, usernameSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    val dateSpannable = date?.let {
        val dateText = formatRelativeTimestamp(date, Date())
        val spannableString = SpannableString(dateText)
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.gray)),
            0,
            dateText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString
    }
    text = SpannableStringBuilder().apply {
        append(usernameSpannable)
        append(" ")
        append(caption)
        dateSpannable?.let{
            append(" ")
            append(it)
        }
    }
    movementMethod = LinkMovementMethod.getInstance() // support for ClickableSpan()
}