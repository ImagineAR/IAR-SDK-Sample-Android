package com.iar.core_sample.ui.fragments.markers

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.iar.common.AppConfig
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.Marker
import com.iar.surface_sdk.SurfaceAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.regex.Pattern
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

    private val _locationMarkers = MutableLiveData<List<Marker>>()
    val locationMarkers: LiveData<List<Marker>>
        get() = _locationMarkers

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

    fun validateLicense(context: Context) {
        SurfaceAPI.validateLicense(
            appConfig.getOrgKeyRegion().first,
            appConfig.getOrgKeyRegion().second,
            context
        )
    }

    fun getLocationMarkers(latitude: Double, longitude: Double, distance: Int) {
        SurfaceAPI.getLocationMarkers(
            latitude,
            longitude,
            distance,
            { markers ->
                _locationMarkers.postValue(markers)
            }
        )
        { errorCode: Int?, errorMessage: String? ->
            Log.i(LOGTAG, "getLocationMarkers: $errorMessage")
            _error.postValue("$errorCode, $errorMessage")
        }

    }

    fun getOnDemandMarkers() {
        CoreAPI.getDemandMarkers("OnDemand",
            { markers ->
                _onDemandMarkers.postValue(markers)
            })
        { errorCode, errorMessage ->
            Log.i(LOGTAG, "OnDemand Markers: $errorCode $errorMessage")
            _error.postValue("$errorCode, $errorMessage")
        }
    }

    fun navigateOnDemandToMarkerDetailsFragment(marker: Marker) {
        val action: NavDirections =
            OnDemandMarkersFragmentDirections.actionOnDemandMarkersFragmentToMarkerDetailsFragment(
                marker
            )
        navigate(action)
    }

    fun navigateLocationToMarkerDetailsFragment(marker: Marker) {
        val action: NavDirections =
            LocationMarkersFragmentDirections.actionLocationMarkersFragmentToMarkerDetailsFragment(
                marker
            )

        navigate(action)
    }

    fun onGetLocationMarkers(coordinates: String) {
        val positionString = coordinates.split("[\\s,]+".toRegex()).toTypedArray()

        var latitude = 0.0
        var longitude = 0.0
        var distance = 0

        if (positionString.size > 2) {
            latitude = positionString[0].toDouble()
            longitude = positionString[1].toDouble()
            distance = positionString[2].toInt()
        }
        getLocationMarkers(latitude, longitude, distance)

    }

}