package com.iar.core_sample.ui.fragments.rewards

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.iar.core_sample.R
import com.iar.core_sample.ui.fragments.usermanagement.UserManagementViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserRewardsFragment : Fragment() {


    private val viewModel by viewModels<UserRewardsViewModel>()

   // private val args by navArgs<UserRewardsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.initialize(requireContext())
        val userId = viewModel.getCurrentUserId()
        println("UserRewardsFragment $userId")
        return inflater.inflate(R.layout.user_rewards_fragment, container, false)
    }



}