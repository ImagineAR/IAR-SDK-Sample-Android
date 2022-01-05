package com.iar.core_sample.ui.fragments.main


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.iar.core_sample.databinding.MainFragmentBinding
import com.iar.core_sample.ui.common.BaseFragment
import com.iar.core_sample.ui.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private val LOGTAG = "MainFragment"
    private val viewModel by viewModels<MainViewModel>()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val (orgKey, region) = viewModel.getOrgKeyRegion()
        Log.d(LOGTAG, orgKey)
        Log.d(LOGTAG, region.toString())

        val binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.userManagementButton.setOnClickListener {
            Log.d(LOGTAG, "user management button clicked")
            //TODO: Navigate to user management screen
        }

        binding.userRewardButton.setOnClickListener {
            Log.d(LOGTAG, "user reward button clicked")
            //TODO: Navigate to user rewards screen
        }
        binding.arHuntButton.setOnClickListener {
            Log.d(LOGTAG, "AR hunt button clicked")
            //TODO: Navigate to arHunt screen
        }
        binding.locationMarkerButton.setOnClickListener {
            Log.d(LOGTAG, "location marker button clicked")
            //TODO: Navigate to location markers screen
        }
        binding.ondemandMarkerButton.setOnClickListener {
            Log.d(LOGTAG, "ondemand marker button clicked")
            //TODO: Navigate to ondemand markers screen
        }
        binding.devToolsButton.setOnClickListener {
            Log.d(LOGTAG, "dev tools button clicked")
            //TODO: Navigate to dev console
        }

        return binding.root
    }

}