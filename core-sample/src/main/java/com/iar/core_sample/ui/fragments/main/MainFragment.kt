package com.iar.core_sample.ui.fragments.main


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.iar.core_sample.BuildConfig
import com.iar.core_sample.databinding.MainFragmentBinding
import com.iar.core_sample.ui.common.BaseFragment
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.iar_core.debugshell.DevConsoleDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment(), DevConsoleDialog.DevConsoleListener {
    private val LOGTAG = "MainFragment"
    private val viewModel by viewModels<MainViewModel>()
    private var devConsoleDialog: DevConsoleDialog? = null

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val (orgKey, region) = viewModel.getOrgKeyRegion()
        Log.d(LOGTAG, orgKey)
        Log.d(LOGTAG, region.toString())

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
            if (devConsoleDialog != null) return@setOnClickListener

            devConsoleDialog = DevConsoleDialog.show(
                parentFragmentManager,
                null,
                "${BuildConfig.APPLICATION_ID}.provider",
                this
            )
        }

        return binding.root
    }

    override fun onDismiss() {
        devConsoleDialog = null
    }

}