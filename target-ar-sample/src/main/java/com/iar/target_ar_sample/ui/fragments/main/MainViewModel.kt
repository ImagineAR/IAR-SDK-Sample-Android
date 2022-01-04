package com.iar.target_ar_sample.ui.fragments.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.iar.target_ar_sample.data.AppConfig
import com.iar.target_ar_sample.ui.common.BaseViewModel
import com.iar.target_ar_sample.utils.NavigationCommand
import com.iar.target_ar_sample.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appConfig: AppConfig) : BaseViewModel() {

    fun initializeCore(context: Context,
                       success: (() -> Unit)? = null,
                       onError: ((errCode: Int, errMsg: String?) -> Unit)? = null) {
        appConfig.initialize(context, success, onError)
    }
}