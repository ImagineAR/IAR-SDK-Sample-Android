package com.iar.target_ar_sample.ui.fragments.main

import android.content.Context
import com.iar.common.AppConfig
import com.iar.target_ar_sample.R
import com.iar.target_ar_sample.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appConfig: AppConfig) : BaseViewModel() {

    fun initializeCore(context: Context,
                       success: (() -> Unit)? = null,
                       onError: ((errCode: Int, errMsg: String?) -> Unit)? = null) {
        appConfig.initialize(context, success, onError)
    }

    fun navigateToUserManagementFragment() {
        navigate(R.id.action_fragment_main_to_userFragment)
    }
}