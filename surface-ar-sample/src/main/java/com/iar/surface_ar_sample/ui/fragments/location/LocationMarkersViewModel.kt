package com.iar.surface_ar_sample.ui.fragments.location

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iar.common.AppConfig
import com.iar.common.Utils
import com.iar.iar_core.Marker
import com.iar.surface_ar_sample.R
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import com.iar.surface_sdk.SurfaceAPI
import com.iar.surface_sdk.aractivity.IARSurfaceActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationMarkersViewModel @Inject constructor(private val appConfig: AppConfig) :
    BaseViewModel() {

    private val LOGTAG = "LocationMarkersViewModel"

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

    fun onGetLocationMarkers(coordinates: String) {
        val positionString = coordinates.split("[\\s,]+".toRegex()).toTypedArray()

        var latitude = 0.0
        var longitude = 0.0

        if (positionString.size > 1) {
            latitude = positionString[0].toDouble()
            longitude = positionString[1].toDouble()
        }

        getLocationMarkers(latitude, longitude)

    }

    fun editTextFilters(editText: EditText) {
        val regex = Regex("[0-9.,-]+")
        editText.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source.filter { regex.matches(it.toString()) }
        })
    }

    fun navigateLocationToSurfaceAR(activity: AppCompatActivity,
                                    marker: Marker,
                                    onComplete: (() -> Unit)? = null) {
        SurfaceAPI.downloadDemandAssetsAndRewards(
            activity,
            marker,
            onSuccess = { assetInfo ->
                val argsBundle = Bundle().apply {
                    putBundle(IARSurfaceActivity.ARG_ASSET_INFO, assetInfo.toExtrasBundle())
                    putString(IARSurfaceActivity.ARG_MARKER, Utils.gson.toJson(marker))
                }

                onComplete?.invoke()
                navigate(R.id.action_locationMarkersFragment_to_activity_surface_ar, argsBundle)
            },
            onFail = { errorMsg ->
                onComplete?.invoke()
                _error.postValue("$errorMsg")
            }
        )
    }
}