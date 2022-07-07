package com.example.prapp.di

import android.app.Application
import com.example.prapp.MainActivity
import com.example.prapp.ui.nointernet.NoInternetFragment
import com.example.prapp.ui.start.StartFragment
import com.example.prapp.ui.web.WebViewFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface AppComponent {

    fun inject(target: MainActivity)
    fun inject(target: StartFragment)
    fun inject(target: WebViewFragment)
    fun inject(target: NoInternetFragment)

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun app(application: Application): Builder

        fun build(): AppComponent
    }

}