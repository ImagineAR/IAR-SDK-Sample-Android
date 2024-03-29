package com.iar.surface_ar_sample.ui.fragments.nfc

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
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

    private val LOGTAG = "NFCViewModel"
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

    var currentMarker: Marker? = null

    fun setIntent(intent: Intent) {
        _currentIntent.postValue(intent)
    }

    fun setNfcController(controller: NFCController) {
        _nfcController.postValue(controller)
    }

    fun setIsWrite(isWrite: Boolean) {
        _isWrite.postValue(isWrite)
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
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun readNfc(controller: NFCController, intent: Intent): MarkerTag? {
        return controller.readNFCMarker(intent)
    }

    fun getMarkerById(markerId: String) {
        getMarkerById(
            markerId,
            { marker ->
                currentMarker = marker
            }
        ) { errorCode: Int?, errorMessage: String? ->

            Log.i(LOGTAG, "Get marker by Id: $errorCode $errorMessage")
            _error.postValue("$errorCode $errorMessage")
        }
    }

    fun navigateNFCToSurfaceAR(
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

    fun navigateToOnDemandMarkersFragment(isNfc: Boolean) {
        val action: NavDirections =
            WriteNFCFragmentDirections.actionWriteNFCFragmentToFragmentOndemand(isNfc)
        navigate(action)
    }
}
