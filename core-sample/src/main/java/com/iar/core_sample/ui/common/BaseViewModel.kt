package com.iar.core_sample.ui.common

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.iar.common.NavigationCommand
import com.iar.common.SingleLiveEvent

open class BaseViewModel : ViewModel() {
    private val _navigationCommand = SingleLiveEvent<NavigationCommand>()
    val navigationCommand: LiveData<NavigationCommand> = _navigationCommand

    // Navigation business logic should be driven from the view models,
    // and handled in the corresponding UI view.
    fun navigateBack() = _navigationCommand.postValue(NavigationCommand.Back)

    fun navigate(direction: Int, args: Bundle? = null) {
        val command = NavigationCommand.To(direction)
        args?.let {
            command.args = it
        }
        _navigationCommand.postValue(command)
    }

    fun navigate(direction: NavDirections) {
        val command = NavigationCommand.ToDir(direction)
        _navigationCommand.postValue(command)
    }

    fun navigateRoot() {
        _navigationCommand.postValue(NavigationCommand.ToRoot)
    }
}