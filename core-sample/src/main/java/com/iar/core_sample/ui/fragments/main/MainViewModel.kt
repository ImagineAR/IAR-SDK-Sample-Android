package com.iar.core_sample.ui.fragments.main


import com.iar.common.AppConfig
import com.iar.core_sample.R
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.iar_core.Region
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appConfig: AppConfig) : BaseViewModel() {

    fun getOrgKeyRegion(): Pair<String, Region> {
        return appConfig.getOrgKeyRegion()
    }

    fun navigateToUserManagementFragment() {
        navigate(R.id.action_mainFragment_to_userManagementFragment)
    }

    fun navigateToUserRewardsFragment() {
        navigate(R.id.action_mainFragment_to_userRewardsFragment)

    }

    fun navigateToARHuntsFragment() {
        navigate(R.id.action_mainFragment_to_ARHuntsFragment)
    }

    fun navigateToOnDemandMarkersFragment() {
        navigate(R.id.action_mainFragment_to_onDemandMarkersFragment)
    }


}