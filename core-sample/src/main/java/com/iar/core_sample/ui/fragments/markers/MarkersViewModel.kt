package com.iar.core_sample.ui.fragments.markers

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.iar.core_sample.data.AppConfig
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.core_sample.ui.fragments.rewards.UserRewardsFragmentDirections
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.Marker
import com.iar.iar_core.Reward
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarkersViewModel @Inject constructor(private val appConfig: AppConfig) :
    BaseViewModel() {

    private val LOGTAG = "MarkersViewModel"

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    private val _onDemandMarkers = MutableLiveData<List<Marker>>()
    val onDemandMarkers: LiveData<List<Marker>>
        get() = _onDemandMarkers

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun initialize(context: Context) {
        CoreAPI.initialize(
            appConfig.getOrgKeyRegion().first,
            appConfig.getOrgKeyRegion().second,
            context
        )
        _userId.postValue(CoreAPI.getCurrentExternalUserId())
    }

    fun getOnDemandMarkers(){
        CoreAPI.getDemandMarkers("OnDemand",
            {markers ->
                _onDemandMarkers.postValue(markers)
            })
            {
             errorCode, errorMessage ->
           Log.i(LOGTAG, "OnDemand Markers: $errorCode $errorMessage")
           _error.postValue("$errorCode, $errorMessage")
            }
    }

    fun navigateOnDemandToMarkerDetailsFragment(marker: Marker, controller: NavController) {
        val action: NavDirections =
            OnDemandMarkersFragmentDirections.actionOnDemandMarkersFragmentToMarkerDetailsFragment(
                marker
            )
        navigate(action, controller)
    }



}