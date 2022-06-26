package es.marcmauri.kliklet.common

import android.content.Context

class SharedPreferencesManager(context: Context) {
    // App preferences file name
    private val PREFERENCES_FILE_NAME = "app_preferences"

    // Key for the variables we'll store in app preferences
    private val KEY_LAST_LATITUDE = "last_latitude"
    private val KEY_LAST_LONGITUDE = "last_longitude"

    // Preferences's instance
    private val preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)


    // All variables we are going to save into app preferences
    var lastLatitude: Float
        get() = preferences.getFloat(KEY_LAST_LATITUDE, 999f)
        set(value) = preferences.edit().putFloat(KEY_LAST_LATITUDE, value).apply()

    var lastLongitude: Float
        get() = preferences.getFloat(KEY_LAST_LONGITUDE, 999f)
        set(value) = preferences.edit().putFloat(KEY_LAST_LONGITUDE, value).apply()

}