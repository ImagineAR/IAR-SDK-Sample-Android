package com.iar.surface_ar_sample.ui.fragments.ondemand

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.iar.common.AppConfig
import com.iar.common.Utils
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.Marker
import com.iar.surface_ar_sample.ui.activities.SurfaceARActivity
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import com.iar.surface_sdk.SurfaceAPI
import com.iar.surface_sdk.aractivity.IARSurfaceActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnDemandMarkersViewModel @Inject constructor(private val appConfig: AppConfig) :
    BaseViewModel() {

    private val LOGTAG = "LocationMarkersViewModel"

    private val _onDemandMarkers = MutableLiveData<List<Marker>>()
    val onDemandMarkers: LiveData<List<Marker>>
        get() = _onDemandMarkers

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    var isValidMarker = false

    fun initialize(context: Context) {
        CoreAPI.initialize(
            appConfig.getOrgKeyRegion().first,
            appConfig.getOrgKeyRegion().second,
            context
        )
        validateLicense(context)
        CoreAPI.getCurrentExternalUserId()?.let {
        }
    }

    private fun validateLicense(context: Context) {
        SurfaceAPI.validateLicense(
            appConfig.getOrgKeyRegion().first,
            appConfig.getOrgKeyRegion().second,
            context
        )
    }

    fun getOnDemandMarkers() {
        val curUser = CoreAPI.getCurrentExternalUserId()
        CoreAPI.getDemandMarkers("OnDemand",
            { markers ->
                _onDemandMarkers.postValue(markers)
            })
        { errorCode, errorMessage ->
            Log.i(LOGTAG, "OnDemand Markers: $errorCode $errorMessage")
            _error.postValue("$errorCode, $errorMessage")
        }
    }

    fun navigateOnDemandToSurfaceAR(
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
                // println("progress $progress")
                onComplete?.invoke(progress)
            }
        )
    }

    fun getMarkerById(
        activity: AppCompatActivity,
        markerId: String,
        onComplete: ((progress: Int) -> Unit)? = null
    ) {
        SurfaceAPI.getMarkerById(markerId,
            { marker ->
                isValidMarker = true
                navigateOnDemandToSurfaceAR(activity, marker, onComplete)
            })
        { errorCode, errorMessage ->
            Log.i(LOGTAG, "Get Marker by ID: $errorCode $errorMessage")
            _error.postValue("$errorCode, $errorMessage")
            isValidMarker = false
        }
    }

    fun navigateToWriteNFCFragment(markerId: String) {
        val action: NavDirections =
            OnDemandMarkersFragmentDirections.actionFragmentOndemandToWriteNFCFragment(markerId)

        navigate(action)
    }
}