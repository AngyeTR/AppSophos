package com.example.appsophos.core.sharedPreferences

import android.app.Application

val prefs: Preferences by lazy {
    SharedApp.prefs!!
}

class SharedApp : Application() {

    companion object {
        var prefs: Preferences? = null
        lateinit var instance: SharedApp
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        prefs = Preferences(applicationContext)
    }

}