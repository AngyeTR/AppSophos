package com.example.appsophos

import android.app.Application
import com.example.appsophos.core.sharedPreferences.Preferences
import dagger.hilt.android.HiltAndroidApp

val prefs: Preferences by lazy {
    SharedApp.prefs!!
}

//@HiltAndroidApp
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