package com.iar.core_sample.ui.fragments.rewards


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.core_sample.databinding.UserRewardsFragmentBinding
import com.iar.iar_core.Reward
import dagger.hilt.android.AndroidEntryPoint
import com.iar.core_sample.R
import com.iar.core_sample.ui.common.BaseFragment
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.core_sample.utils.Util
import com.iar.core_sample.utils.Util.addDivider

@AndroidEntryPoint
class UserRewardsFragment : BaseFragment() {


    private val viewModel by viewModels<UserRewardsViewModel>()

    private lateinit var binding: UserRewardsFragmentBinding
    private lateinit var rewardList: RecyclerView
    private var userRewards: List<Reward>? = null
    override fun getViewModel(): BaseViewModel = viewModel

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
                Util.showToastMessage("There is error $error", requireContext())
            }
        })

        binding.getRewardButton.setOnClickListener {
            setupDialog()
        }

        return binding.root
    }

    private fun setupRewards(rewards: List<Reward>) {
        rewardList.layoutManager = LinearLayoutManager(requireContext())

        rewardList.addDivider(requireContext())
        val adapter =
            UserRewardsAdapter(rewards, object : UserRewardsAdapter.OnRewardItemClickListener {
                override fun onRewardItemClick(reward: Reward) {
                    viewModel.navigateToRewardDetailsFragment(
                        reward
                    )
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
            onGetUserReward(inputId)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.create().show()
    }

    private fun onGetUserReward(inputId: String) {
        userRewards?.let {
            val reward = viewModel.getRewardFromId(inputId, it)
            if (reward != null) {
                viewModel.navigateToRewardDetailsFragment(reward)

            } else {

                Util.showToastMessage("Don't have this reward id", requireContext())
            }
        }
    }

}