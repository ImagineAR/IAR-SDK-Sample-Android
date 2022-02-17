package com.iar.core_sample.ui.fragments.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.iar.common.SettingsFragment
import com.iar.core_sample.BuildConfig
import com.iar.core_sample.databinding.MainFragmentBinding
import com.iar.core_sample.ui.common.BaseFragment
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.core_sample.ui.fragments.usermanagement.UserManagementViewModel
import com.iar.iar_core.debugshell.DevConsoleDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private val viewModel by viewModels<MainViewModel>()
    private val userManagementViewModel by viewModels<UserManagementViewModel>()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        userManagementViewModel.initialize(requireContext())
        userManagementViewModel.loadCurrentUser(requireContext())

        val binding = MainFragmentBinding.inflate(inflater, container, false)

        binding.userManagementButton.setOnClickListener {
            viewModel.navigateToUserManagementFragment()
        }

        binding.userRewardButton.setOnClickListener {
            viewModel.navigateToUserRewardsFragment()
        }
        binding.arHuntButton.setOnClickListener {
            viewModel.navigateToARHuntsFragment()
        }
        binding.locationMarkerButton.setOnClickListener {
            viewModel.navigateToLocationMarkersFragment()
        }
        binding.ondemandMarkerButton.setOnClickListener {
            viewModel.navigateToOnDemandMarkersFragment()
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