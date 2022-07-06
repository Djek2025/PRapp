package com.example.prapp.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.prapp.utils.SPCache
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Application): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideSPCache(sp: SharedPreferences): SPCache = SPCache(sp)

}