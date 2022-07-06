package com.example.prapp

import android.app.Application
import com.example.prapp.di.AppComponent
import com.example.prapp.di.DaggerAppComponent

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
    }
}