package com.example.appsophos.core.sharedPreferences

import android.content.Context
import android.content.SharedPreferences

class Preferences (context: Context) {

        val APP_PREF_EMAIL = "emailPreferencesFile"
        val EmailPreferences : SharedPreferences = context.getSharedPreferences(APP_PREF_EMAIL, Context.MODE_PRIVATE)

        val APP_PREF_NAME = "NamePreferencesFile"
        val NamePreferences : SharedPreferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)


    var emailPref: String?
            get() = EmailPreferences.getString(APP_PREF_EMAIL, "" )
            set(value) = EmailPreferences.edit().putString(APP_PREF_EMAIL, value).apply()

    var namePref: String?
        get() = NamePreferences.getString(APP_PREF_NAME, "" )
        set(value) = NamePreferences.edit().putString(APP_PREF_NAME, value).apply()
}