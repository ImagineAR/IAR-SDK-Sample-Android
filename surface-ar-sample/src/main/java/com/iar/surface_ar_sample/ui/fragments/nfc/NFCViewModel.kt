package com.iar.surface_ar_sample.ui.fragments.nfc

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iar.common.AppConfig
import com.iar.common.Utils
import com.iar.iar_core.Marker
import com.iar.nfc_sdk.MarkerTag
import com.iar.nfc_sdk.MarkerType
import com.iar.nfc_sdk.NFCController
import com.iar.surface_ar_sample.ui.activities.SurfaceARActivity
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import com.iar.surface_sdk.SurfaceAPI
import com.iar.surface_sdk.SurfaceAPI.getMarkerById
import com.iar.surface_sdk.aractivity.IARSurfaceActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NFCViewModel @Inject constructor(private val appConfig: AppConfig) :
    BaseViewModel() {

    private val _currentIntent = MutableLiveData<Intent>()

    val currentIntent: LiveData<Intent>
        get() = _currentIntent

    private val _nfcController = MutableLiveData<NFCController>()

    val nfcController: LiveData<NFCController>
        get() = _nfcController

    private val _isWrite = MutableLiveData<Boolean>()
    val isWrite: LiveData<Boolean>
        get() = _isWrite

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _marker = MutableLiveData<Marker>()
    val marker: LiveData<Marker>
        get() = _marker

    fun setIntent(intent: Intent) {
        _currentIntent.postValue(intent)
    }

    fun setNfcController(controller: NFCController) {
        _nfcController.postValue(controller)
    }

    fun validateLicense(context: Context) {
        SurfaceAPI.validateLicense(
            appConfig.getOrgKeyRegion().first,
            appConfig.getOrgKeyRegion().second,
            context
        )
    }

    fun startNfc(controller: NFCController, write: Boolean) {
        _isWrite.postValue(write)
        controller.startReadingNFC()
    }

    fun writeNfc(markerId: String, controller: NFCController, intent: Intent): String {
        var message = ""
        val markerTag = MarkerTag(markerId, MarkerType.ONDEMAND)

        val isWritten = controller.writeNFCMarker(markerTag, intent)

        if (isWritten) {
            message = "Write NFC successfully,  $markerTag"
        }
        return message
    }

    fun closeKeyBoard(view: View, activity: AppCompatActivity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    fun readNfc(controller: NFCController, intent: Intent) : MarkerTag?{
        return controller.readNFCMarker(intent)
    }

    fun getMarkerById(markerId: String ) {
        getMarkerById(
            markerId,
            { marker ->
                _marker.postValue(marker)
            }
        ) { errorCode: Int?, errorMessage: String? ->

            _error.postValue("$errorMessage")
        }
    }

    fun navigateNFCToSurfaceAR(activity: AppCompatActivity,
                                    marker: Marker,
                                    onComplete: (() -> Unit)? = null) {

        SurfaceAPI.downloadDemandAssetsAndRewards(
            activity,
            marker,
            onSuccess = { assetInfo ->
               // println
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

}
