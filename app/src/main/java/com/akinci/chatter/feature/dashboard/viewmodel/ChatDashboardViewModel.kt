package com.akinci.chatter.feature.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatDashboardViewModel @Inject constructor() : ViewModel() {

    init {
        Timber.d("ChatDashboardViewModel created..")
    }


}