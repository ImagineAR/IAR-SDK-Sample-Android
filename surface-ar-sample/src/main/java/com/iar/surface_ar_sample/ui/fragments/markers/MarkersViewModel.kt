package com.iar.surface_ar_sample.ui.fragments.markers

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iar.common.AppConfig
import com.iar.iar_core.Marker
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import com.iar.surface_sdk.SurfaceAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarkersViewModel @Inject constructor(private val appConfig: AppConfig) :
    BaseViewModel() {

    private val LOGTAG = "MarkersViewModel"

    private val _locationMarkers = MutableLiveData<List<Marker>>()
    val locationMarkers: LiveData<List<Marker>>
        get() = _locationMarkers

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun validateLicense(context: Context) {
        SurfaceAPI.validateLicense(
            appConfig.getOrgKeyRegion().first,
            appConfig.getOrgKeyRegion().second,
            context
        )
    }

    fun getLocationMarkers(latitude: Double, longitude: Double) {
        SurfaceAPI.getLocationMarkers(
            latitude,
            longitude,
            10000,
            { markers ->
                _locationMarkers.postValue(markers)

            }
        )
        { errorCode: Int?, errorMessage: String? ->
            Log.i(LOGTAG, "getLocationMarkers: $errorMessage")
            _error.postValue("$errorCode, $errorMessage")
        }

    }
}