package com.iar.core_sample.ui.fragments.rewards

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.iar.common.AppConfig
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.Reward
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserRewardsViewModel @Inject constructor(private val appConfig: AppConfig) :
    BaseViewModel() {

    private val _userRewards = MutableLiveData<List<Reward>>()
    val userRewards: LiveData<List<Reward>>
        get() = _userRewards

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val LOGTAG = "User Rewards"

    fun initialize(context: Context) {
        CoreAPI.initialize(
            appConfig.getOrgKeyRegion().first,
            appConfig.getOrgKeyRegion().second,
            context
        )
        CoreAPI.getCurrentExternalUserId()?.let {
            _userId.postValue(it)
        }
    }

    fun getUserRewards() {
        CoreAPI.getRewardsForCurrentUser(
            { rewards ->
                _userRewards.postValue(rewards)
            }
        )
        { errorCode, errorMessage ->
            Log.i(LOGTAG, "getReward: $errorMessage")
            _error.postValue("$errorCode, $errorMessage")
        }
    }


    fun getRewardFromId(rewardId: String, rewards: List<Reward>): Reward? {
        var currentReward: Reward? = null
        for (reward in rewards) {
            if (rewardId == reward.id) {
                currentReward = reward
            }
        }
        return currentReward
    }

    fun navigateToRewardDetailsFragment(reward: Reward) {
        val action: NavDirections =
            UserRewardsFragmentDirections.actionUserRewardsFragmentToRewardDetailsFragment(
                reward
            )
        navigate(action)
    }

}