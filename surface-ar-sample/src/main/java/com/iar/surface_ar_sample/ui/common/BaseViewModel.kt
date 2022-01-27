package com.iar.surface_ar_sample.ui.common

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.iar.common.NavigationCommand
import com.iar.common.SingleLiveEvent

open class BaseViewModel: ViewModel() {
    // Mutable live data should always be private
    private val _navigationCommand = SingleLiveEvent<NavigationCommand>()
    // Any observers need to go through the LiveData interface and not the
    // MutableLiveData for best practice, to not expose mutability to unnecessary
    // classes.
    val navigationCommand: LiveData<NavigationCommand> = _navigationCommand


    // Navigation business logic should be driven from the view models,
    // and handled in the corresponding UI view.
    fun navigateBack() = _navigationCommand.postValue(NavigationCommand.Back)

    fun navigate(direction: Int, args: Bundle? = null) {
        val command = NavigationCommand.To(direction)
        command.args = args
        _navigationCommand.postValue(command)
    }

    fun navigateRoot() {
        _navigationCommand.postValue(NavigationCommand.ToRoot)
    }
}