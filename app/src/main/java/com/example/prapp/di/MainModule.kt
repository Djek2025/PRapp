package com.example.prapp.di

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.prapp.repository.NetworkStateRepo
import com.example.prapp.ui.BaseViewModel
import com.example.prapp.utils.SPCache
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    @Provides
    @Singleton
    fun provideScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Provides
    @Singleton
    fun provideNetworkStateRepo(context: Application, scope: CoroutineScope) = NetworkStateRepo(context, scope)

    @Provides
    @Singleton
    fun provideBaseVM(sp: SPCache, repo: NetworkStateRepo) = object : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BaseViewModel(repo) as T
        }
    }.create(BaseViewModel::class.java)

}