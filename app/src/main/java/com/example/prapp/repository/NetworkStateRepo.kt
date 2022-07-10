package com.example.prapp.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.RemoteException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NetworkStateRepo(
    private val context: Context,
    private val scope: CoroutineScope
) {

    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var callback: ConnectivityManager.NetworkCallback? = null
    private var receiver: ConnectivityReceiver? = null

    private val _state = MutableSharedFlow<NetworkState>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val state = _state.asSharedFlow()


    init {
        _state
            .subscriptionCount
            .map { count -> count > 0 }
            .distinctUntilChanged()
            .onEach { isActive ->
                if (isActive) subscribe()
                else unsubscribe()
            }
            .launchIn(scope)
    }


    fun hasNetworkConnection(): Boolean = getCurrentNetwork() == NetworkState.Connected

    private fun getCurrentNetwork(): NetworkState {
        return try {
            cm.getNetworkCapabilities(cm.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .let { connected ->
                    if (connected == true) NetworkState.Connected
                    else NetworkState.Disconnected
                }
        } catch (e: RemoteException) {
            NetworkState.Disconnected
        }
    }

    private fun subscribe() {

        if (callback != null || receiver != null) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            callback = NetworkCallbackImpl().also { cm.registerDefaultNetworkCallback(it) }
        } else {
            receiver = ConnectivityReceiver().also {
                context.registerReceiver(it, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            }
        }
        emitNetworkState(getCurrentNetwork())
    }


    private fun unsubscribe() {

        if (callback == null && receiver == null) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            callback?.run { cm.unregisterNetworkCallback(this) }
            callback = null
        } else {
            receiver?.run { context.unregisterReceiver(this) }
            receiver = null
        }
    }

    private fun emitNetworkState(newState: NetworkState) {
        scope.launch(Dispatchers.Main) {
            _state.emit(newState)
        }
    }

    private inner class ConnectivityReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            intent.getParcelableExtra<NetworkInfo>(ConnectivityManager.EXTRA_NETWORK_INFO)?.isConnectedOrConnecting
                .let { connected ->
                    if (connected == true) emitNetworkState(NetworkState.Connected)
                    else emitNetworkState(getCurrentNetwork())
                }
        }

    }

    private inner class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) = emitNetworkState(NetworkState.Connected)

        override fun onLost(network: Network) = emitNetworkState(NetworkState.Disconnected)
    }


    sealed class NetworkState {
        object Connected : NetworkState()
        object Disconnected : NetworkState()
    }
}