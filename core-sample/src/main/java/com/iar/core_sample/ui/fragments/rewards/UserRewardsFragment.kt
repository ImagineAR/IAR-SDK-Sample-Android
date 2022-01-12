package com.iar.core_sample.ui.fragments.rewards


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.core_sample.databinding.UserRewardsFragmentBinding
import com.iar.iar_core.Reward
import dagger.hilt.android.AndroidEntryPoint
import com.iar.core_sample.R
import com.iar.core_sample.utils.Util

@AndroidEntryPoint
class UserRewardsFragment : Fragment() {


    private val viewModel by viewModels<UserRewardsViewModel>()

    private lateinit var binding: UserRewardsFragmentBinding
    private lateinit var rewardList: RecyclerView
    private var userRewards: List<Reward>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = UserRewardsFragmentBinding.inflate(inflater, container, false)
        rewardList = binding.rewardList

        //Initialize and load user Id
        viewModel.initialize(requireContext())

        viewModel.userId.observe(viewLifecycleOwner, { userId ->
            userId?.let { viewModel.getUserRewards() }

        })

        viewModel.userRewards.observe(viewLifecycleOwner, { rewards ->
            setupRewards(rewards)
            userRewards = rewards
        })

        viewModel.error.observe(viewLifecycleOwner, { error ->
            error?.let {
                val toast = Toast.makeText(
                    requireActivity().getApplicationContext(),
                    "There is error $error",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        })

        binding.getRewardButton.setOnClickListener {
            setupDialog()
        }

        return binding.root
    }


    private fun setupRewards(rewards: List<Reward>) {
        rewardList.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )

        rewardList.addItemDecoration(dividerItemDecoration)

        val adapter =
            UserRewardsAdapter(rewards, object : UserRewardsAdapter.OnRewardItemClickListener {
                override fun onRewardItemClick(reward: Reward) {
                    navigateToRewardDetailsFragment(reward)
                }
            })

        rewardList.adapter = adapter

    }

    private fun setupDialog() {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("Get User Reward")
        val container = FrameLayout(requireActivity())
        val editText: EditText = Util.setupDialogEditText(requireContext())
        container.addView(editText)
        builder.setView(container)
        builder.setMessage("Enter reward ID")
        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
            val inputId = editText.text.toString()

            userRewards?.let {
                val reward = getRewardFromId(inputId, it)
                if (reward != null) {
                    navigateToRewardDetailsFragment(reward)
                } else {
                    val toast = Toast.makeText(
                        requireActivity().getApplicationContext(),
                        "Don't have this reward id",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                }
            }

            dialogInterface.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.create().show()
    }

    private fun navigateToRewardDetailsFragment(reward: Reward) {
        val action =
            UserRewardsFragmentDirections.actionUserRewardsFragmentToRewardDetailsFragment(
                reward
            )
        binding.root.findNavController().navigate(action)
    }

    private fun getRewardFromId(rewardId: String, rewards: List<Reward>): Reward? {
        var currentReward: Reward? = null
        for (reward in rewards) {
            if (rewardId == reward.id) {
                currentReward = reward
            }
        }
        return currentReward
    }

}