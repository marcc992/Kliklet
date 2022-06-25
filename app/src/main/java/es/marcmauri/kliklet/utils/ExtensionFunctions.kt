package es.marcmauri.kliklet.utils

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, duration).show()

fun Activity.toast(resourceId: Int, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, resourceId, duration).show()

fun Fragment.snackBar(
    message: CharSequence,
    view: View,
    duration: Int = Snackbar.LENGTH_SHORT,
    action: String? = null,
    actionEvt: (v: View) -> Unit = {}
) {
    val snackBar = Snackbar.make(view, message, duration)
    if (!action.isNullOrEmpty()) {
        snackBar.setAction(action, actionEvt)
    }
    snackBar.show()
}

fun String.asSentence() =
    if (this.isNotEmpty()) this.first().uppercaseChar() + this.substring(1).lowercase()
    else this