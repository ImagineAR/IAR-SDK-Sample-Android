package com.iar.target_ar_sample.ui.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.iar.target_ar_sample.R
import com.iar.target_ar_sample.utils.NavigationCommand

/**
 * This base fragment will handle the basic navigation
 * options available
 */
abstract class BaseFragment : Fragment() {
    abstract fun getViewModel(): BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeNavigation()
    }

    private fun observeNavigation() {
        getViewModel().navigationCommand.observe(
            viewLifecycleOwner
        ) {
            val navController = NavHostFragment.findNavController(this@BaseFragment)
            val args = it.args

            when (it) {
                is NavigationCommand.ToRoot -> {
                    navController.navigate(R.id.fragment_main, args)
                }
                is NavigationCommand.To -> {
                    // The direction should have an argument in it's structure if it needs to
                    // so we dont have to pass in args.
                    navController.navigate(it.direction)
                }
                is NavigationCommand.Back -> {
                    // If our backstack is empty (navigateUp() is false), just finish our activity.
                    // This is the same behaviour when pressing the system
                    // back button.
                    if (!navController.navigateUp()) activity?.finish()
                }
                else -> {
                    // Ignore.
                }
            }
        }
    }
}