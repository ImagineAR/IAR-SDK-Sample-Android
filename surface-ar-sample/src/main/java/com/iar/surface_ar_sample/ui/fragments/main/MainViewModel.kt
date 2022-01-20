package com.iar.surface_ar_sample.ui.fragments.main

import android.content.Context
import com.iar.common.AppConfig
import com.iar.surface_ar_sample.ui.common.BaseViewModel
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