package com.iar.core_sample.ui.fragments.arhunts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.iar.core_sample.databinding.FragmentHuntDetailsBinding
import com.iar.core_sample.utils.Util
import com.iar.iar_core.Hunt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HuntDetailsFragment : Fragment() {

    private lateinit var binding: FragmentHuntDetailsBinding
    private val args by navArgs<HuntDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentHuntDetailsBinding.inflate(inflater, container, false)
        val huntString = args.hunt
      val hunt =  Util.gson.fromJson(huntString, Hunt::class.java)
        println(hunt.name)
        println(hunt.id)
        println(hunt.description)
        println(hunt.thumbnailUrl)
        println(hunt.huntMarkers?.size)
        val huntMarkers = hunt.huntMarkers
        huntMarkers?.let{
            for(huntMarker in it){
                println(huntMarker.clueCard?.id)
                println(huntMarker.clueCard?.description)
                println(huntMarker.clueCard?.name)
                println(huntMarker.huntId)
                println(huntMarker.id)
                println(huntMarker.marker.toString())
                println(huntMarker.markerId)
                println(huntMarker.scanned)
            }
        }
        println(hunt.huntRewards?.size)
        val huntRewards = hunt.huntRewards
        huntRewards?.let{
            for(huntReward in it){
                println(huntReward.createdAt)
                println(huntReward.huntId)
                println(huntReward.id)
                println(huntReward.isCustomProgress)
                println(huntReward.requiredScanCount)
                println(huntReward.reward.toString())
                println(huntReward.rewardId)

            }
        }
        println(hunt.endDate)
        println(hunt.retroactiveContribution)
        println(hunt.startDate)


        return  binding.root
    }


}