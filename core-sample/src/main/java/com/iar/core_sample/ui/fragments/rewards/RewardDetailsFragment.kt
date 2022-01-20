package com.iar.core_sample.ui.fragments.rewards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.iar.core_sample.databinding.RewardDetailsFragmentBinding
import com.iar.core_sample.utils.Util.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardDetailsFragment : Fragment() {

    private lateinit var binding: RewardDetailsFragmentBinding
    private val args by navArgs<RewardDetailsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = RewardDetailsFragmentBinding.inflate(inflater, container, false)

        val reward = args.reward

        if (reward.type == "Image") {
            binding.rewardImageLayout.visibility = View.VISIBLE
            reward.image?.let {
                binding.rewardCard.loadImage(it.url, requireContext())
            }

        } else if (reward.type == "General Promotion Code") {
            binding.promoCodeRewardLayout.visibility = View.VISIBLE
            binding.promoCode.text = reward.generalPromoCode.promoCode
            binding.optionalText.text = reward.generalPromoCode.optionalText

        }

        binding.rewardType.text = reward.type
        val name = "Reward Name: ${reward.name}"
        binding.rewardName.text = name
        val id = "Reward Id: ${reward.id}"
        binding.rewardId.text = id
        val action = "ActionButtonEnabled: ${reward.actionButtonEnabled}\n" +
                "ActionButtonText: ${reward.actionButtonText}\nActionButtonUrl: ${reward.actionButtonUrl}"
        binding.rewardAction.text = action
        val rewardReason = "Reward Reason: ${reward.rewardReasonType}"
        binding.rewardReason.text = rewardReason

        return binding.root
    }

}