package com.iar.target_ar_sample.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.analytics.AnalyticsController
import com.iar.iar_core.controllers.DebugSettingsController
import com.iar.iar_core.debugshell.DevConsoleDialog
import com.iar.target_ar_sample.BuildConfig
import com.iar.target_ar_sample.R
import com.iar.target_ar_sample.databinding.FragmentMainBinding
import com.iar.target_ar_sample.ui.common.BaseFragment
import com.iar.target_ar_sample.ui.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment(), DevConsoleDialog.DevConsoleListener {
    private val viewModel by viewModels<MainViewModel>()

    private var devConsoleDialog: DevConsoleDialog? = null

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let {
            // Initialize CoreAPI when we start.
            viewModel.initializeCore(it)
        }

        val binding = FragmentMainBinding.inflate(inflater, container, false)


        binding.scanButton.setOnClickListener {
            viewModel.navigate(R.id.action_target_ar)
        }

        binding.userButton.setOnClickListener {
            //TODO: Navigate to user info
        }

        binding.devToolsButton.setOnClickListener {
            if (devConsoleDialog != null) return@setOnClickListener

            devConsoleDialog = DevConsoleDialog.show(parentFragmentManager,
                null,
                "${BuildConfig.APPLICATION_ID}.provider",
                this)
        }

        return binding.root
    }

    /**
     * DevConsoleDialog.DevConsoleListener
     */
    override fun onDismiss() {
        devConsoleDialog = null
    }
}