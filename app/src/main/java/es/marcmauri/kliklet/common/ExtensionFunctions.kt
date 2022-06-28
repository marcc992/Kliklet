package es.marcmauri.kliklet.common

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import es.marcmauri.kliklet.app.prefs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

/*
    COMMON FUNCTIONALITIES
*/

/**
 * Show a SnackBar.
 *
 * @param message: Text to show
 * @param view: View where place the message
 * @param duration: (Optional) Duration of the message (by default is SnackBar.LENGTH_SHORT)
 * @param action: (Optional) Text for the action button. If so, then give the event actionEvt
 * @param actionEvt: (Optional) Code to execute when the action button is clicked
 */
fun snackBar(
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

/**
 * This method return the distance in Kilometers between itself and the location passed by param.
 *
 * @param destination: LatLng with the destination point.
 * @return The distance to the 'destination' in Kilometers.
 */
fun LatLng.distanceToInKm(destination: LatLng): Double {
    val origin = this
    val theta = origin.longitude - destination.longitude
    var dist = sin(origin.latitude.deg2rad()) * sin(destination.latitude.deg2rad()) +
            cos(origin.latitude.deg2rad()) * cos(destination.latitude.deg2rad()) * cos(theta.deg2rad())
    dist = acos(dist).rad2deg() * 60 * 1.1515
    return dist * 1.609344 // constant for Kilometers
}


/*
    PRIMITIVES
*/

/**
 * Convert the current String in a Sentence format
 */
fun String.asSentence() =
    if (this.isNotEmpty()) this.first().uppercaseChar() + this.substring(1).lowercase()
    else this

/**
 * Convert the first char of the current String to uppercase
 */
fun String.capitalizeFirst() =
    if (this.isNotEmpty()) this.first().uppercaseChar() + this.substring(1)
    else this

/**
 * Convert the current double from Degrees to Radian
 */
fun Double.deg2rad() = this * Math.PI / 180.0

/**
 * Convert the current double from Radian to Degrees
 */
fun Double.rad2deg() = this * 180.0 / Math.PI


/*
    GOOGLE SERVICES AND PERMISSIONS
*/

/**
 * It checks the availability of Google Services on the current device.
 *
 * @return A boolean TRUE if GServices are available. FALSE in the other case.
 */
fun Activity.checkGoogleServicesAvailability() = GoogleApiAvailability.getInstance()
    .isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS

/**
 * This method gets the last known location from the user and stores it to the user preferences.
 * It must be called just after the user location permissions request, either FINE or COARSE.
 */
@SuppressLint("MissingPermission") //todo
fun Activity.getLastKnownLocation() {
    LocationServices.getFusedLocationProviderClient(this).lastLocation
        .addOnSuccessListener { location ->
            if (location != null) {
                prefs.lastLocation = LatLng(location.latitude, location.longitude)
            }
        }
}

/**
 * This method checks if the user has already grant the location permissions. If not, then it asks
 * for them.
 *
 * @param onGrantedEvt: Code to execute when permissions were granted
 * @param onRefusedEvt: Code to execute when permissions were refused
 * @param onGServicesNotAvailable: Code to execute when no Google Services available
 */
fun FragmentActivity.checkLocationPermissions(
    onGrantedEvt: () -> Unit,
    onRefusedEvt: () -> Unit,
    onGServicesNotAvailable: () -> Unit
) {
    if (checkGoogleServicesAvailability()) {
        val locationPermissionRequest = this.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            val locationPermissionsGranted = when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> true
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> true
                else -> false
            }

            if (locationPermissionsGranted)
                onGrantedEvt()
            else
                onRefusedEvt()
        }

        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    } else {
        onGServicesNotAvailable()
    }
}