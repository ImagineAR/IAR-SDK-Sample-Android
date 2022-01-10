package com.iar.core_sample.ui.fragments.usermanagement

import android.content.Context
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
    var isAnonymous = true

    fun initialize(context: Context) {
        CoreAPI.initialize(appConfig.getOrgKeyRegion().first, appConfig.getOrgKeyRegion().second, context)
    }

    fun createNewUser(
        context: Context,
        externalUserId: String,
        success: () -> Unit,
        failure: (errCode: Int, errMsg: String?) -> Unit
    ) {

        CoreAPI.createExternalUserId(User(externalUserId), onSuccess = {
            CoreAPI.setExternalUserId(externalUserId, true)
            saveUserId(context, externalUserId, false, false)
            success()
        }, onFail = failure)

    }

    fun saveUserId(context: Context, userId: String, initialized: Boolean, isAnon: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(Constants.KEY_USER_ID, userId)
            .putBoolean(Constants.KEY_IS_ANONYMOUS, isAnon)
            .apply()

    }

    fun getSavedUserId(context: Context): String? {
        isAnonymous = getIsAnonymous(context)
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(Constants.KEY_USER_ID, null)
    }


    fun login(
        context: Context,
        externalUserId: String,
        success: () -> Unit,
        failure: (errCode: Int, errMsg: String?) -> Unit
    ) {

        var isSameIdAsCurrent = false

        getCurrentUserId().let {
            isSameIdAsCurrent = it == externalUserId
        }

        if (getIsAnonymous(context) && !isSameIdAsCurrent) {
            // Migrate data if current session is anonymous.
            getCurrentUserId().let { curUserId ->
                CoreAPI.migrateDataFrom(curUserId, externalUserId, onSuccess = {
                    CoreAPI.setExternalUserId(externalUserId, true)
                    saveUserId(context, externalUserId, false, false)
                    success()
                }, onFail = failure)
                return@login
            }
        }

        saveUserId(context, externalUserId, true, false)
        CoreAPI.setExternalUserId(externalUserId, true)

        success.invoke()
    }

    fun getCurrentUserId(): String {
        return CoreAPI.getCurrentExternalUserId() ?: UUID.randomUUID().toString()
    }

    fun getIsAnonymous(context: Context): Boolean {
        isAnonymous = PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean(Constants.KEY_IS_ANONYMOUS, true)
        return isAnonymous
    }

    fun logout(
        context: Context,
        success: () -> Unit,
        failure: (errCode: Int, errMsg: String?) -> Unit
    ) {

        isAnonymous = true;
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(Constants.KEY_IS_ANONYMOUS, isAnonymous)
            .apply()
        anonymousSession(context, success, failure)

    }

    private fun anonymousSession(
        context: Context,
        success: () -> Unit,
        onError: (errCode: Int, errMsg: String?) -> Unit
    ) {
        val newUUID = UUID.randomUUID().toString()
        CoreAPI.createExternalUserId(
            User(newUUID),
            {
                saveUserId(context, newUUID, false, true)
                CoreAPI.setExternalUserId(newUUID, true)
                success.invoke()
            },
            onError
        )

    }

    fun migrateUser(
        context: Context,
        oldUserId: String,
        newUserId: String,
        success: () -> Unit,
        failure: (errCode: Int, errMsg: String?) -> Unit
    ) {

        CoreAPI.migrateDataFrom(
            oldUserId,
            newUserId,
            onSuccess = {
                CoreAPI.setExternalUserId(newUserId, true)
                saveUserId(context, newUserId, false, false)
                success()
            }, onFail = failure
        )

    }

    fun loadCurrentUser(
        context: Context,
        success: () -> Unit,
        failure: (errCode: Int, errMsg: String?) -> Unit
    ) {


        getSavedUserId(context)?.let { extUser ->
            // If saved user is anonymous, just set the externalId.
            if (getIsAnonymous(context)) {
                CoreAPI.setExternalUserId(extUser, true)
                success.invoke()
            } else {
                login(context, extUser, success, failure)
            }
            return@loadCurrentUser
        }

        // If we don't have a current user id, start an anonymous session.
        anonymousSession(context, success, failure)
    }

}