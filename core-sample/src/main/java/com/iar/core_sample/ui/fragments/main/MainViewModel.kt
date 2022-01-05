package com.iar.core_sample.ui.fragments.main

import com.iar.core_sample.data.AppConfig
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.iar_core.Region
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appConfig: AppConfig): BaseViewModel() {

    fun getOrgKeyRegion(): Pair<String, Region>{
        return appConfig.getOrgKeyRegion()
    }
}