package com.iar.core_sample.ui.fragments.arhunts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.gson.GsonBuilder
import com.iar.core_sample.databinding.FragmentHuntRewardsBinding
import com.iar.core_sample.utils.Util
import com.iar.core_sample.utils.Util.loadImage
import com.iar.iar_core.HuntReward


class HuntRewardDetailsFragment : Fragment() {

    private lateinit var binding: FragmentHuntRewardsBinding
    private val args by navArgs<HuntRewardDetailsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHuntRewardsBinding.inflate(inflater, container, false)

        val huntRewardString = args.huntReward
        val huntReward = Util.gson.fromJson(huntRewardString, HuntReward::class.java)
        huntReward?.reward?.image?.url?.let {
            binding.rewardImage.loadImage(it, requireContext())
        }
        binding.rewardName.text = huntReward?.reward?.name
        val typeString = "Type: ${huntReward?.reward?.type}"
        binding.rewardType.text = typeString
        binding.rewardId.text = huntReward?.reward?.id

        val gson = GsonBuilder().setPrettyPrinting().create()
        val huntRewardData = gson.toJson(huntReward)
        binding.huntRewardData.text = huntRewardData

        return binding.root
    }

}