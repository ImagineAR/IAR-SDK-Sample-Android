package com.iar.target_ar_sample.data

import android.content.Context
import ca.iversoft.iar_core.CoreAPI
import ca.iversoft.iar_core.Region
import ca.iversoft.iar_core.User
import java.util.*

/**
 * Simple class to manage the initialization/configuration
 * of IAR core/IAR target
 */
class AppConfig {
    private val QA_ORG_KEY = "pk_org_c7a686bf2b4a4a3c95b817cbf95b1198"
    private val QA_REGION = Region.QA

    fun initialize(context: Context,
                   success: (() -> Unit)?,
                   onError: ((errCode: Int, errMsg: String?) -> Unit)?) {
        // First we initialize the Core API.
        CoreAPI.initialize(QA_ORG_KEY, QA_REGION, context)

        // Then we need to create a user (or use an existing one. Here we just create a new one everytime.)
        val newUUID = UUID.randomUUID().toString()
        CoreAPI.createExternalUserId(
            User(newUUID),
            {
                CoreAPI.setExternalUserId(newUUID, true)
                success?.invoke()
            },
            { erCode, erMsg ->
                // If we have a handler, pass it through
                onError?.invoke(erCode, erMsg)
            }
        )
    }
}