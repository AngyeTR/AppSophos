package com.example.appsophos.core.sharedPreferences

import android.content.Context
import android.content.SharedPreferences

class Preferences (context: Context) {

    val APP_PREF_EMAIL = "emailPreferencesFile"
    val EmailPreferences : SharedPreferences = context.getSharedPreferences(APP_PREF_EMAIL, Context.MODE_PRIVATE)

    val APP_PREF_NAME = "NamePreferencesFile"
    val NamePreferences : SharedPreferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE)

    val APP_PREF_PASSWORD = "passwordPreferencesFile"
    var PasswordPreferences : SharedPreferences = context.getSharedPreferences(APP_PREF_PASSWORD, Context.MODE_PRIVATE)

    val APP_PREF_MODE = "modePreferencesFile"
    var ModePreferences : SharedPreferences = context.getSharedPreferences(APP_PREF_MODE, Context.MODE_PRIVATE)

    val APP_PREF_LANG = "langPreferencesFile"
    var LangPreferences : SharedPreferences = context.getSharedPreferences(APP_PREF_LANG, Context.MODE_PRIVATE)

    var emailPref: String?
            get() = EmailPreferences.getString(APP_PREF_EMAIL, "" )
            set(value) = EmailPreferences.edit().putString(APP_PREF_EMAIL, value).apply()

    var namePref: String?
        get() = NamePreferences.getString(APP_PREF_NAME, "" )
        set(value) = NamePreferences.edit().putString(APP_PREF_NAME, value).apply()

    var passwordPref: String?
        get() = PasswordPreferences.getString(APP_PREF_PASSWORD, "" )
        set(value) = PasswordPreferences.edit().putString(APP_PREF_PASSWORD, value).apply()

    var modePref: Boolean
        get() = ModePreferences.getBoolean(APP_PREF_MODE, false )
        set(value) = ModePreferences.edit().putBoolean(APP_PREF_MODE, value).apply()

    var langPref: String?
        get() = LangPreferences.getString(APP_PREF_LANG, "es" )
        set(value) = LangPreferences.edit().putString(APP_PREF_LANG, value).apply()
}