package com.iar.surface_ar_sample.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.iar.common.SettingsFragment
import com.iar.surface_ar_sample.BuildConfig
import com.iar.surface_ar_sample.R
import com.iar.surface_ar_sample.databinding.FragmentMainBinding
import com.iar.surface_ar_sample.ui.common.BaseFragment
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private val viewModel by viewModels<MainViewModel>()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAffinity()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        context?.let {
            // Initialize CoreAPI when we start.
            viewModel.initializeCore(it)
        }

        val binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.locationMarkers.setOnClickListener {
            viewModel.navigateToLocationMarkersFragment()
        }

        binding.onDemandMarkers.setOnClickListener {
            viewModel.navigate(R.id.action_to_ondemand)
        }

        binding.nfcWrite.setOnClickListener {
            viewModel.navigateToWriteNFCFragment()
        }

        binding.nfcRead.setOnClickListener {
            viewModel.navigateToReadNFCFragment()
        }

        binding.userButton.setOnClickListener {
            viewModel.navigateToUserManagementFragment()
        }

        binding.devToolsButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(android.R.id.content, SettingsFragment(BuildConfig.APPLICATION_ID), null)
                ?.addToBackStack(SettingsFragment::class.java.name)
                ?.commit()

        }

        return binding.root
    }

}