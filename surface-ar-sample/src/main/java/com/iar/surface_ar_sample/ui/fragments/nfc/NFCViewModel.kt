package com.iar.surface_ar_sample.ui.fragments.nfc

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iar.common.AppConfig
import com.iar.nfc_sdk.MarkerTag
import com.iar.nfc_sdk.MarkerType
import com.iar.nfc_sdk.NFCController
import com.iar.surface_ar_sample.ui.common.BaseViewModel
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

    fun setIntent(intent: Intent) {
        _currentIntent.postValue(intent)
    }

    fun setNfcController(controller: NFCController) {
        _nfcController.postValue(controller)
    }

    fun startNfc(controller: NFCController) {
        controller.startReadingNFC()
    }

    fun writeNfc(markerId: String, controller: NFCController, intent: Intent): String {
        var message = ""
        val markerTag = MarkerTag(markerId, MarkerType.ONDEMAND)

        val isWritten = controller.writeNFCMarker(markerTag, intent)

        if (isWritten) {
            message = "Write NFC successfully,  $markerTag"
            _isWrite.postValue(true)
        }
        return message
    }

    fun closeKeyBoard(view: View, activity: AppCompatActivity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

    fun readNfc(controller: NFCController, intent: Intent) : MarkerTag?{
        _isWrite.postValue(false)
        return controller.readNFCMarker(intent)
    }

}
