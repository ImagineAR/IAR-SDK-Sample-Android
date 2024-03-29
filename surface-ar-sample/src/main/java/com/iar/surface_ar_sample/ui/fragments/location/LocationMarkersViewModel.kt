package com.iar.surface_ar_sample.ui.fragments.location

import android.content.Context
import android.content.Intent
import android.text.InputFilter
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iar.common.AppConfig
import com.iar.common.Utils
import com.iar.iar_core.Marker
import com.iar.iar_core.controllers.DebugSettingsController
import com.iar.surface_ar_sample.ui.activities.SurfaceARActivity
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

    var isValidCoordinates: Boolean = false

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

    fun navigateLocationToSurfaceAR(
        activity: AppCompatActivity,
        marker: Marker,
        onComplete: ((progress: Int) -> Unit)? = null
    ) {
        SurfaceAPI.downloadDemandAssetsAndRewards(
            activity,
            marker,
            onSuccess = { assetInfo ->
                val intent = Intent(activity, SurfaceARActivity::class.java).apply {
                    putExtras(assetInfo.toExtrasBundle())
                    putExtra(IARSurfaceActivity.ARG_MARKER, Utils.gson.toJson(marker))
                }

                navigate(intent)
            },
            onFail = { errorMsg ->
                _error.postValue("$errorMsg")
            },
            onProgress = { progress ->
                Log.i(LOGTAG, "progress $progress")
                onComplete?.invoke(progress)
            }
        )
    }

    fun getCoordinates(): String {
        val latitude = DebugSettingsController.simulatedLatitude
        val longitude = DebugSettingsController.simulatedLongitude
        return "$latitude,$longitude"
    }
}