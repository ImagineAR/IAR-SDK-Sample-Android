package com.iar.surface_ar_sample.ui.fragments.ondemand

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iar.common.AppConfig
import com.iar.common.Utils
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.Marker
import com.iar.surface_ar_sample.R
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import com.iar.surface_sdk.SurfaceAPI
import com.iar.surface_sdk.aractivity.IARSurfaceActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnDemandMarkersViewModel @Inject constructor(private val appConfig: AppConfig) :
    BaseViewModel() {

    private val LOGTAG = "MarkersViewModel"

    private val _onDemandMarkers = MutableLiveData<List<Marker>>()
    val onDemandMarkers: LiveData<List<Marker>>
        get() = _onDemandMarkers

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

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

    fun navigateOnDemandToSurfaceAR(activity: AppCompatActivity,
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
                navigate(R.id.action_surface_ar, argsBundle)
            },
            onFail = { errorMsg ->
                onComplete?.invoke()
                _error.postValue("$errorMsg")
            }
        )
    }
}