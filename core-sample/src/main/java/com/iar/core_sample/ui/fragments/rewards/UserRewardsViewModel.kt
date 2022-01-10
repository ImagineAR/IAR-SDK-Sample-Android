package com.iar.core_sample.ui.fragments.rewards

import android.content.Context
import androidx.lifecycle.ViewModel
import com.iar.core_sample.data.AppConfig
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.Region
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UserRewardsViewModel @Inject constructor(private val appConfig: AppConfig) :
    BaseViewModel() {

    fun initialize(context: Context) {
        CoreAPI.initialize(appConfig.getOrgKeyRegion().first, appConfig.getOrgKeyRegion().second, context)
    }

    fun getCurrentUserId(): String {
        return CoreAPI.getCurrentExternalUserId() ?: UUID.randomUUID().toString()
    }
}