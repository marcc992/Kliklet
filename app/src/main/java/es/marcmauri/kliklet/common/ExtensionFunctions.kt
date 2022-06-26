package es.marcmauri.kliklet.common

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

private fun showSnackBar(
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

fun Activity.snackBar(
    message: CharSequence,
    view: View,
    duration: Int = Snackbar.LENGTH_SHORT,
    action: String? = null,
    actionEvt: (v: View) -> Unit = {}
) = showSnackBar(message, view, duration, action, actionEvt)

fun Fragment.snackBar(
    message: CharSequence,
    view: View,
    duration: Int = Snackbar.LENGTH_SHORT,
    action: String? = null,
    actionEvt: (v: View) -> Unit = {}
) = showSnackBar(message, view, duration, action, actionEvt)

fun String.asSentence() =
    if (this.isNotEmpty()) this.first().uppercaseChar() + this.substring(1).lowercase()
    else this


fun Double.deg2rad() = this * Math.PI / 180.0

fun Double.rad2deg() = this * 180.0 / Math.PI

/**
 * This method return the distance in Kilometers between 2 positions LatLng
 *
 * @param destination: LatLng with the destination point
 * @return The distance in Kilometers
 */
fun LatLng.distanceToInKm(destination: LatLng): Double {
    val origin = this
    val theta = origin.longitude - destination.longitude
    var dist = sin(origin.latitude.deg2rad()) * sin(destination.latitude.deg2rad()) +
            cos(origin.latitude.deg2rad()) * cos(destination.latitude.deg2rad()) * cos(theta.deg2rad())
    dist = acos(dist).rad2deg() * 60 * 1.1515
    return dist * 1.609344 // constant for Kilometers
}