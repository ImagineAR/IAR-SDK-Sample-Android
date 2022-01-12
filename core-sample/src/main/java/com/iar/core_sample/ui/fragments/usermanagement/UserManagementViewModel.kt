package com.iar.core_sample.ui.fragments.usermanagement

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.iar.core_sample.data.AppConfig
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.core_sample.utils.Constants
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.User
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UserManagementViewModel @Inject constructor(private val appConfig: AppConfig) :
    BaseViewModel() {

    private val _isAnonymous = MutableLiveData<Boolean>()
    val isAnonymous: LiveData<Boolean>
        get() = _isAnonymous

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean>
        get() = _isLogin

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun initialize(context: Context) {
        CoreAPI.initialize(
            appConfig.getOrgKeyRegion().first,
            appConfig.getOrgKeyRegion().second,
            context
        )
    }

    fun createNewUser(
        context: Context,
        externalUserId: String
    ) {
        CoreAPI.createExternalUserId(User(externalUserId), onSuccess = {
            CoreAPI.setExternalUserId(externalUserId, true)
            saveUserId(context, externalUserId, false)
            _isLogin.postValue(true)

        }, onFail = { errCode, errMsg ->
            _error.postValue("$errCode, $errMsg")
        }
        )
    }


    private fun saveUserId(context: Context, userId: String, isAnon: Boolean) {
        _userId.postValue(userId)
        _isAnonymous.postValue(isAnon)
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(Constants.KEY_USER_ID, userId)
            .putBoolean(Constants.KEY_IS_ANONYMOUS, isAnon)
            .apply()

    }

    private fun getSavedUserId(context: Context): String? {
        _isAnonymous.postValue(
            PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(Constants.KEY_IS_ANONYMOUS, true)
        )
        _userId.postValue(
            PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Constants.KEY_USER_ID, null)
        )
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(Constants.KEY_USER_ID, null)
    }


    fun login(
        context: Context,
        externalUserId: String
    ) {

        var isSameIdAsCurrent: Boolean = false

        getCurrentUserId().let {
            isSameIdAsCurrent = it == externalUserId
        }
        _isLogin.postValue(true)

        if (getIsAnonymous(context) && !isSameIdAsCurrent) {

            // Migrate data if current session is anonymous.
            getCurrentUserId().let { curUserId ->
                CoreAPI.migrateDataFrom(curUserId, externalUserId, onSuccess = {
                    CoreAPI.setExternalUserId(externalUserId, true)
                    saveUserId(context, externalUserId, false)

                }, onFail = { errCode, errMsg ->
                    _error.postValue("$errCode, $errMsg")
                })
                return@login
            }
        }

        saveUserId(context, externalUserId, false)
        CoreAPI.setExternalUserId(externalUserId, true)

    }

    private fun getCurrentUserId(): String {
        return CoreAPI.getCurrentExternalUserId() ?: UUID.randomUUID().toString()
    }

    private fun getIsAnonymous(context: Context): Boolean {
        val anonymousState = PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(Constants.KEY_IS_ANONYMOUS, false)
        _isAnonymous.postValue(anonymousState)
        return anonymousState
    }

    fun logout(context: Context) {
        _isLogin.postValue(false)
        _isAnonymous.postValue(true)
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(Constants.KEY_IS_ANONYMOUS, true)
            .apply()
        anonymousSession(context)
    }

    private fun anonymousSession(context: Context) {
        val newUUID = UUID.randomUUID().toString()
        CoreAPI.createExternalUserId(
            User(newUUID),
            onSuccess = {
                saveUserId(context, newUUID, true)
                CoreAPI.setExternalUserId(newUUID, true)

            },
            onFail = { errCode, errMsg ->
                _error.postValue("$errCode, $errMsg")
            })

    }

    fun migrateUser(
        context: Context,
        oldUserId: String,
        newUserId: String
    ) {

        CoreAPI.migrateDataFrom(
            oldUserId,
            newUserId,
            onSuccess = {
                CoreAPI.setExternalUserId(newUserId, true)
                saveUserId(context, newUserId, false)
                _isLogin.postValue(true)

            }, onFail = { errCode, errMsg ->
                _error.postValue("$errCode, $errMsg")
            })

    }

    fun loadCurrentUser(context: Context) {
        getSavedUserId(context)?.let { extUser ->
            // If saved user is anonymous, just set the externalId.
            if (getIsAnonymous(context)) {
                CoreAPI.setExternalUserId(extUser, true)

            } else {
                login(context, extUser)
            }
            return@loadCurrentUser
        }

        // If we don't have a current user id, start an anonymous session.
        anonymousSession(context)
    }

}