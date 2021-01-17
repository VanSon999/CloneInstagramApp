@file:Suppress("DEPRECATION")

package vanson.dev.instagramclone.Views

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_password.view.*
import vanson.dev.instagramclone.R

class PasswordDialog : DialogFragment() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onPasswordConfirm(password: String)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity.layoutInflater.inflate(R.layout.dialog_password, null)

        return AlertDialog.Builder(context).setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                mListener.onPasswordConfirm(view.password_dialog.text.toString())
            }.setNegativeButton(android.R.string.cancel) { _, _ ->

            }.setTitle(R.string.please_enter_password).create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as Listener
    }
}