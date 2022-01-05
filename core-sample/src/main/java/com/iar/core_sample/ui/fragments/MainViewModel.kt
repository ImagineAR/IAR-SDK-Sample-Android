package com.iar.core_sample.ui.fragments

import androidx.lifecycle.ViewModel
import com.iar.core_sample.data.AppConfig
import com.iar.iar_core.Region
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appConfig: AppConfig): ViewModel() {

    fun getOrgKeyRegion(): Pair<String, Region>{
        return appConfig.getOrgKeyRegion()
    }
}