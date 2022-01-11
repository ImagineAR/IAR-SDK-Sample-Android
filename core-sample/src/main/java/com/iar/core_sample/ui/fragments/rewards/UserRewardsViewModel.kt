package com.iar.core_sample.ui.fragments.rewards

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iar.core_sample.R
import com.iar.core_sample.data.AppConfig
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.Reward
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UserRewardsViewModel @Inject constructor(private val appConfig: AppConfig) :
    BaseViewModel() {

   private val _userRewards = MutableLiveData<List<Reward>>()
    val userRewards: LiveData<List<Reward>>
    get() = _userRewards

    private val LOGTAG = "User Rewards"

    fun initialize(context: Context) {
        CoreAPI.initialize(appConfig.getOrgKeyRegion().first, appConfig.getOrgKeyRegion().second, context)
    }

    fun getCurrentUserId(): String {



        return CoreAPI.getCurrentExternalUserId() ?: UUID.randomUUID().toString()
    }


     fun getUserRewards() {
        CoreAPI.getRewardsForCurrentUser(
            { rewards ->
                _userRewards.postValue(rewards)

            }
        )  // Error callback.
        { errorCode, errorMessage ->
            Log.i(LOGTAG, "getReward: $errorMessage")

        }
    }




    fun navigateToRewardDetailsFragment() {
        navigate(R.id.action_userRewardsFragment_to_rewardDetailsFragment)
    }

}