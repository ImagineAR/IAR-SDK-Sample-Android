package com.iar.core_sample.ui.fragments.rewards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.iar.common.Utils.loadImage
import com.iar.core_sample.R
import com.iar.core_sample.databinding.RewardDetailsFragmentBinding
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
        val name = "${getString(R.string.reward_name)} ${reward.name}"
        binding.rewardName.text = name
        val id = "${R.string.reward_id} ${reward.id}"
        binding.rewardId.text = id
        val action = "${getString(R.string.action_button_enabled)} ${reward.actionButtonEnabled}\n" +
                "${getString(R.string.action_button_text)} ${reward.actionButtonText}\n${getString(R.string.action_button_url)} ${reward.actionButtonUrl}"
        binding.rewardAction.text = action
        val rewardReason = "${getString(R.string.reward_reason)} ${reward.rewardReasonType}"
        binding.rewardReason.text = rewardReason

        return binding.root
    }

}