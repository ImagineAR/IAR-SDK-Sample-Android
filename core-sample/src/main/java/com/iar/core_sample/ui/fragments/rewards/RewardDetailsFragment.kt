package com.iar.core_sample.ui.fragments.rewards


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
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
        val reward = args.reward
        println(reward.id)
        println(reward.name)
        println(reward.type)
        println(reward.actionButtonEnabled)
        println(reward.actionButtonText)
        println(reward.actionButtonUrl)
        println(reward.image.url)
        reward.generalPromoCode?.let {
            println(reward.generalPromoCode.promoCode)
            println(reward.generalPromoCode.optionalText)
        }
        println(reward.rewardReasonType)
        binding = RewardDetailsFragmentBinding.inflate(inflater,container, false)

        return binding.root
    }



}