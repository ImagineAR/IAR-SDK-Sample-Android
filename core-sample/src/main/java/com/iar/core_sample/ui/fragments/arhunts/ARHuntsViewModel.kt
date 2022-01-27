package com.iar.core_sample.ui.fragments.arhunts

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iar.common.AppConfig
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.core_sample.utils.Util
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.CoreAPI.getAllHunts
import com.iar.iar_core.Hunt
import com.iar.iar_core.HuntMarker
import com.iar.iar_core.HuntReward
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun navigateToARHuntDetailsFragment(hunt: Hunt) {
        val huntString = Util.gson.toJson(hunt)
        val action =
            ARHuntsFragmentDirections.actionARHuntsFragmentToHuntDetailsFragment(huntString)

        navigate(action)
    }

    fun navigateToHuntMarkerFragment(huntMarker: HuntMarker) {
        val huntMarkerString = Util.gson.toJson(huntMarker)
        val action = ARHuntDetailsFragmentDirections.actionHuntDetailsFragmentToHuntMarkersFragment(
            huntMarkerString
        )

        navigate(action)
    }

    fun navigateToHuntRewardFragment(huntReward: HuntReward) {
        val huntRewardString = Util.gson.toJson(huntReward)
        val action = ARHuntDetailsFragmentDirections.actionHuntDetailsFragmentToHuntRewardsFragment(
            huntRewardString
        )
        navigate(action)
    }

    fun getHuntFromId(huntId: String, hunts: ArrayList<Hunt>): Hunt? {
        var currentHunt: Hunt? = null
        for (hunt in hunts) {
            if (huntId == hunt.id) {
                currentHunt = hunt
            }
        }
        return currentHunt
    }

}