package com.example.prapp.ui

import androidx.lifecycle.ViewModel
import com.example.prapp.repository.NetworkStateRepo

class BaseViewModel(netRepo: NetworkStateRepo): ViewModel() {

    val networkState = netRepo.state

}