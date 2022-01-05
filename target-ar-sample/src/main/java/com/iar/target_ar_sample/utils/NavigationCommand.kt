package com.iar.target_ar_sample.utils

import android.os.Bundle

sealed class NavigationCommand {
    var args: Bundle? = null

    data class To(val direction: Int) : NavigationCommand()
    object Back : NavigationCommand()
    object ToRoot : NavigationCommand()
}