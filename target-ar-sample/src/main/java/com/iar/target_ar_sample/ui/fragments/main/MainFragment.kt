package com.iar.target_ar_sample.ui.fragments.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.iar.iar_core.CoreAPI
import com.iar.target_ar_sample.R
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
            viewModel.navigate(R.id.action_target_ar)
        }

        binding.userButton.setOnClickListener {
            showUserDialog()
        }

        binding.devToolsButton.setOnClickListener {
            //TODO: Navigate to dev console
        }

        return binding.root
    }

    private fun showUserDialog() {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.dialog_title_user))
        builder.setMessage("User ID: ${CoreAPI.getCurrentExternalUserId()}")

        builder.setPositiveButton(getString(R.string.button_ok)) { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        builder.create().show()
    }
}