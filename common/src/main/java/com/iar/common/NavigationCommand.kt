package com.iar.common

import android.os.Bundle
import androidx.navigation.NavDirections

sealed class NavigationCommand {
    var args: Bundle? = null

    data class To(val direction: Int) : NavigationCommand()
    data class ToDir(val direction: NavDirections): NavigationCommand()
    object Back : NavigationCommand()
    object ToRoot : NavigationCommand()
}