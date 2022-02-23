package com.iar.surface_ar_sample.ui.fragments.ondemand

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        onComplete: (() -> Unit)? = null
    ) {
        SurfaceAPI.downloadDemandAssetsAndRewards(
            activity,
            marker,
            onSuccess = { assetInfo ->
                val intent = Intent(activity, SurfaceARActivity::class.java).apply {
                    putExtras(assetInfo.toExtrasBundle())
                    putExtra(IARSurfaceActivity.ARG_MARKER, Utils.gson.toJson(marker))
                }

                onComplete?.invoke()
                navigate(intent)
            },
            onFail = { errorMsg ->
                onComplete?.invoke()
                _error.postValue("$errorMsg")
            }
        )
    }

    fun getMarkerById(
        activity: AppCompatActivity,
        markerId: String,
        onComplete: (() -> Unit)? = null
    ) {
        SurfaceAPI.getMarkerById(markerId,
            { marker ->
                navigateOnDemandToSurfaceAR(activity, marker, onComplete)
            })
        { errorCode, errorMessage ->
            Log.i(LOGTAG, "Get Marker by ID: $errorCode $errorMessage")
            _error.postValue("$errorCode, $errorMessage")
        }
    }
}