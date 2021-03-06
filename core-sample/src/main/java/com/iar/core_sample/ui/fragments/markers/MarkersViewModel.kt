package com.iar.core_sample.ui.fragments.markers

import android.content.Context
import android.text.InputFilter
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.iar.common.AppConfig
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.Marker
import com.iar.iar_core.controllers.DebugSettingsController
import com.iar.surface_sdk.SurfaceAPI
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

    private val _locationMarkers = MutableLiveData<List<Marker>>()
    val locationMarkers: LiveData<List<Marker>>
        get() = _locationMarkers

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    var isValidCoordinates: Boolean = false

    private val _marker = MutableLiveData<Marker>()
    val marker: LiveData<Marker>
        get() = _marker

    fun initialize(context: Context) {
        CoreAPI.initialize(
            appConfig.getOrgKeyRegion().first,
            appConfig.getOrgKeyRegion().second,
            context
        )
        validateLicense(context)
        CoreAPI.getCurrentExternalUserId()?.let {
            _userId.postValue(it)
        }
    }

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

    fun getMarkerById(markerId: String) {
        SurfaceAPI.getMarkerById(markerId,
            { marker ->
                navigateOnDemandToMarkerDetailsFragment(marker)
            })
        { errorCode, errorMessage ->
            Log.i(LOGTAG, "Get Marker by ID: $errorCode $errorMessage")
            _error.postValue("$errorCode, $errorMessage")
        }
    }

    fun getMarkerDetail(markerId: String) {
        SurfaceAPI.getMarkerById(markerId,
            { marker ->
                _marker.postValue(marker)
            })
        { errorCode, errorMessage ->
            Log.i(LOGTAG, "Get Marker Info: $errorCode $errorMessage")
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

        if (positionString.size > 1) {
            try {
                latitude = positionString[0].toDouble()
                longitude = positionString[1].toDouble()
                isValidCoordinates = true
            } catch (e: NumberFormatException) {
                _error.postValue(". Please enter valid coordinates.")
                isValidCoordinates = false
            }
        } else {
            _error.postValue(". Please enter valid coordinates.")
            isValidCoordinates = false
        }

        getLocationMarkers(latitude, longitude)

    }

    fun editTextFilters(editText: EditText) {
        val regex = Regex("[0-9.,-]+")
        editText.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source.filter { regex.matches(it.toString()) }
        })
    }

    fun getCoordinates(): String {
        val latitude = DebugSettingsController.simulatedLatitude
        val longitude = DebugSettingsController.simulatedLongitude
        return "$latitude,$longitude"
    }

}