package com.iar.core_sample.ui.fragments.main


import com.iar.common.AppConfig
import com.iar.core_sample.R
import com.iar.core_sample.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appConfig: AppConfig) : BaseViewModel() {

    fun navigateToUserManagementFragment() {
        navigate(R.id.action_mainFragment_to_userFragment)
    }

    fun navigateToUserRewardsFragment() {
        navigate(R.id.action_mainFragment_to_userRewardsFragment)

    }

    fun navigateToARHuntsFragment() {
        navigate(R.id.action_mainFragment_to_ARHuntsFragment)
    }

    fun navigateToLocationMarkersFragment() {
        navigate(R.id.action_mainFragment_to_locationMarkersFragment)
    }

    fun navigateToOnDemandMarkersFragment() {
        navigate(R.id.action_mainFragment_to_onDemandMarkersFragment)
    }


}