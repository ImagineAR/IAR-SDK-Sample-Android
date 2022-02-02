package com.iar.common

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavDirections

sealed class NavigationCommand {
    var args: Bundle? = null

    data class To(val direction: Int) : NavigationCommand()
    data class ToDir(val direction: NavDirections): NavigationCommand()
    data class ToActivity(val intent: Intent): NavigationCommand()
    object Back : NavigationCommand()
    object ToRoot : NavigationCommand()
}