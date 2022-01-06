package com.iar.target_ar_sample.ui.activities

import com.iar.iar_core.Region
import com.iar.target_ar_sample.data.AppConfig
import com.iar.target_ar_sample.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TargetARViewModel @Inject constructor(private val appConfig: AppConfig): BaseViewModel() {

    fun getRegion(): Region = appConfig.getCurRegion()

    fun getLicense(): String = appConfig.getCurLicense()
}