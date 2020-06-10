package app.storytel.candidate.com.commondialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import app.storytel.candidate.com.R

class TimeOutDialog(context: Context, f: () -> Unit) {

    private var dialog: AlertDialog

    init {
        dialog = AlertDialog.Builder(context)
                .setTitle(R.string.connection_time_out)
                .setMessage(R.string.internet_error_message)
                .setPositiveButton(R.string.retry) { _, _ ->
                    f.invoke()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .create()
    }

    fun show() {
        dialog.show()
    }
}