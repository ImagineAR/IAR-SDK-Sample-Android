package com.iar.core_sample.ui.fragments.rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.core_sample.databinding.UserRewardsFragmentBinding
import com.iar.core_sample.ui.fragments.main.MainFragmentDirections
import com.iar.iar_core.Reward
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserRewardsFragment : Fragment() {


    private val viewModel by viewModels<UserRewardsViewModel>()
    private lateinit var binding: UserRewardsFragmentBinding
    private lateinit var rewardList: RecyclerView

            override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.initialize(requireContext())
        val userId = viewModel.getCurrentUserId()
        println("UserRewardsFragment $userId")

        viewModel.getUserRewards()
        binding = UserRewardsFragmentBinding.inflate(inflater, container, false)
        rewardList = binding.rewardList

        viewModel.userRewards.observe(viewLifecycleOwner, { rewards ->
            setupRewards(rewards)
        })
         return binding.root
    }


    private fun setupRewards(rewards: List<Reward>){
        rewardList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = UserRewardsAdapter(rewards, object: UserRewardsAdapter.OnRewardItemClickListener{
            override fun onRewardItemClick(reward: Reward) {
                println("user reward clicked ${reward.id}")
         //      viewModel.navigateToRewardDetailsFragment()

                val action = UserRewardsFragmentDirections.actionUserRewardsFragmentToRewardDetailsFragment()
                binding.root.findNavController().navigate(action)
            }
        })
        rewardList.adapter = adapter


    }


}