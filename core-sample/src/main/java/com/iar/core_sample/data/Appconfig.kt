package com.iar.core_sample.data

import com.iar.iar_core.Region

class AppConfig {

    private   val QA_ORG_KEY = "pk_org_c7a686bf2b4a4a3c95b817cbf95b1198"
    private   val QA_REGION = Region.QA

    fun getOrgKeyRegion(): Pair<String, Region> {
        return Pair(QA_ORG_KEY,QA_REGION )
    }
}