package com.iar.core_sample.ui.fragments.arhunts

import android.content.Context
import android.os.UserManager
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iar.core_sample.data.AppConfig
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.core_sample.ui.fragments.usermanagement.UserManagementViewModel
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.CoreAPI.getAllHunts
import com.iar.iar_core.Hunt
import com.iar.iar_core.controllers.FileLogger.log
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@HiltViewModel
class ARHuntsViewModel @Inject constructor(private val appConfig: AppConfig) :
    BaseViewModel() {

    private val LOGTAG = "AR Hunts"

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    private val _arHunts = MutableLiveData<ArrayList<Hunt>>()
    val arHunts: LiveData<ArrayList<Hunt>>
        get() = _arHunts

    private val _arSingleHunt = MutableLiveData<Hunt>()
    val arSingleHunt: LiveData<Hunt>
        get() = _arSingleHunt

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun initialize(context: Context) {
        CoreAPI.initialize(
            appConfig.getOrgKeyRegion().first,
            appConfig.getOrgKeyRegion().second,
            context
        )
        _userId.postValue(CoreAPI.getCurrentExternalUserId())
    }


    fun getARHunts() {
        getAllHunts(1,
            50,
            { hunts ->
                _arHunts.postValue(hunts)
                Log.i(LOGTAG, "hunt success " + hunts.size)
            })
        { errorCode: Int?, errorMessage: String? ->
            Log.i(LOGTAG, "Get user hunts: $errorCode, $errorMessage")
            _error.postValue("Get user hunts: $errorCode, $errorMessage")
        }

    }

    fun getSingleHunt(huntId: String) {
        CoreAPI.getHunt(huntId,
            { hunt: Hunt? ->
                Log.i(LOGTAG, "Get single hunt successfully")
                _arSingleHunt.postValue(hunt)
            }
        )  // Error callback.
        { errorCode: Int?, errorMessage: String? ->
            Log.i(LOGTAG, "Get single hunt: $errorCode, $errorMessage")
            _error.postValue("Get single hunt: $errorCode, $errorMessage")
        }
    }

}