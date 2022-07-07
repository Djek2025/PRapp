package com.example.prapp

import android.app.Application
import com.appsflyer.AppsFlyerLib
import com.example.prapp.di.AppComponent
import com.example.prapp.di.DaggerAppComponent
import com.onesignal.OneSignal

class App: Application() {

    private val dagger: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .app(this)
            .build()
    }

    fun getDaggerComponent() = dagger

    override fun onCreate() {
        super.onCreate()

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId(BuildConfig.ONE_SIGNAL_APP_ID)

        AppsFlyerLib.getInstance().let {
            it.init(BuildConfig.APPS_FLYER_DEV_KEY, null, this)
            it.start(this)
            it.setDebugLog(true)
        }
    }
}