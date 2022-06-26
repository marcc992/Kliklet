package es.marcmauri.kliklet.common

import android.content.Context
import com.google.android.gms.maps.model.LatLng

class SharedPreferencesManager(context: Context) {
    // App preferences file name
    private val PREFERENCES_FILE_NAME = "app_preferences"

    // Key for the variables we'll store in app preferences
    private val KEY_LAST_LATITUDE = "last_latitude"
    private val KEY_LAST_LONGITUDE = "last_longitude"

    // Preferences's instance
    private val preferences =
        context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)


    // All variables we are going to save into app preferences
    private var lastLatitude: Double
        get() = preferences.getFloat(KEY_LAST_LATITUDE, 999f).toDouble()
        set(value) = preferences.edit().putFloat(KEY_LAST_LATITUDE, value.toFloat()).apply()

    private var lastLongitude: Double
        get() = preferences.getFloat(KEY_LAST_LONGITUDE, 999f).toDouble()
        set(value) = preferences.edit().putFloat(KEY_LAST_LONGITUDE, value.toFloat()).apply()

    fun isLastLocationReady() = lastLatitude < 200 && lastLongitude < 200

    var lastLocation: LatLng
        get() = LatLng(lastLatitude, lastLongitude)
        set(value) {
            lastLatitude = value.latitude
            lastLongitude = value.longitude
        }
}