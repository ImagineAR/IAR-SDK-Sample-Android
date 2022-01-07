package com.iar.target_ar_sample.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.iar.target_ar_sample.databinding.MainFragmentBinding
import com.iar.target_ar_sample.ui.common.BaseFragment
import com.iar.target_ar_sample.ui.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment() {
    private val viewModel by viewModels<MainViewModel>()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context?.let {
            // Initialize CoreAPI when we start.
            viewModel.initializeCore(it)
        }

        val binding = MainFragmentBinding.inflate(inflater, container, false)


        binding.scanButton.setOnClickListener {
            //TODO: Navigate to scan target AR.
        }

        binding.userButton.setOnClickListener {
            //TODO: Navigate to user info
        }

        binding.devToolsButton.setOnClickListener {
            //TODO: Navigate to dev console
        }

        return binding.root
    }

}