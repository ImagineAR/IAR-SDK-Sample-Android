package com.example.target_ar_sample.ui.fragments.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.target_ar_sample.data.AppConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel : ViewModel() {
    @Inject
    lateinit var appConfig: AppConfig

    fun initializeCore(context: Context,
                       success: (() -> Unit)? = null,
                       onError: ((errCode: Int, errMsg: String?) -> Unit)? = null) {
        appConfig.initialize(context, success, onError)
    }
}