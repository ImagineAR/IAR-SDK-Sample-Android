package com.iar.surface_ar_sample.ui.fragments.main

import android.content.Context
import com.iar.common.AppConfig
import com.iar.surface_ar_sample.R
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import com.iar.surface_sdk.SurfaceAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appConfig: AppConfig) : BaseViewModel() {
    private var isInitialized = false

    fun initializeCore(context: Context,
                       success: (() -> Unit)? = null,
                       onError: ((errCode: Int, errMsg: String?) -> Unit)? = null) {
        if (!isInitialized) {
            appConfig.initialize(context, {
                isInitialized = true
                success?.invoke()
            }, onError)

            validateLicense(context)
        }
    }

    /**
     * Initialize surface API authorization.
     */
    private fun validateLicense(context: Context) {
        SurfaceAPI.validateLicense(
            appConfig.getOrgKeyRegion().first,
            appConfig.getOrgKeyRegion().second,
            context
        )
    }

    fun navigateToLocationMarkersFragment() {
        navigate(R.id.action_fragment_main_to_locationMarkersFragment)
    }
}