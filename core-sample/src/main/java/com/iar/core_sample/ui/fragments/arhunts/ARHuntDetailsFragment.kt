package com.iar.core_sample.ui.fragments.arhunts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.core_sample.databinding.FragmentHuntDetailsBinding


import com.iar.core_sample.utils.Util
import com.iar.core_sample.utils.Util.loadImage
import com.iar.iar_core.Hunt
import com.iar.iar_core.HuntMarker
import com.iar.iar_core.HuntReward
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ARHuntDetailsFragment : Fragment() {

    private val viewModel by viewModels<ARHuntsViewModel>()


    private lateinit var binding: FragmentHuntDetailsBinding
    private val args by navArgs<ARHuntDetailsFragmentArgs>()
    private lateinit var huntMarkerListView: RecyclerView
    private lateinit var huntRewardListView: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentHuntDetailsBinding.inflate(inflater, container, false)
        val huntString = args.hunt
      val hunt =  Util.gson.fromJson(huntString, Hunt::class.java)
        println(hunt.name)
//        println(hunt.id)
//        println(hunt.description)
//        println(hunt.thumbnailUrl)
        binding.huntName.text = hunt.name
        binding.huntId.text = hunt.id
        binding.huntDescription.text = hunt.description
        binding.huntImage.loadImage(hunt.thumbnailUrl, requireContext())

        binding.huntMarkerTitle.text = "Hunt Markers"
        huntMarkerListView = binding.huntMarkerList

//        println(hunt.huntMarkers?.size)
        val huntMarkers = hunt.huntMarkers

        huntMarkers?.let{
            setupHuntMarkers(it)
//            for(huntMarker in it){
//                huntMarker.clueCard?.imageUrl
////                println(huntMarker.clueCard?.id)
////                println(huntMarker.clueCard?.description)
////                println(huntMarker.clueCard?.name)
////                println(huntMarker.huntId)
////                println(huntMarker.id)
////                println(huntMarker.marker.toString())
////                println(huntMarker.markerId)
////                println(huntMarker.scanned)
//            }
        }
        binding.huntRewardTitle.text = "Hunt Rewards"
        huntRewardListView = binding.huntRewardList
      //  println(hunt.huntRewards?.size)
        val huntRewards = hunt.huntRewards
        huntRewards?.let{
            setupHuntRewards(it)
  //          for(huntReward in it){
              //  huntReward.reward.image.
//                println(huntReward.createdAt)
//                println(huntReward.huntId)
//                println(huntReward.id)
//                println(huntReward.isCustomProgress)
//                println(huntReward.requiredScanCount)
//                println(huntReward.reward.toString())
//                println(huntReward.rewardId)

  //          }
        }
//        println(hunt.endDate)
//        println(hunt.retroactiveContribution)
//        println(hunt.startDate)


        return  binding.root
    }

    private fun setupHuntMarkers(huntMarkers: ArrayList<HuntMarker>) {
        huntMarkerListView.layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
        huntMarkerListView.addItemDecoration(dividerItemDecoration)

        val adapter = HuntMarkersAdapter(huntMarkers, object : HuntMarkersAdapter.OnHuntMarkerItemClickListener {
            override fun onHuntMarkerItemClick(huntMarker: HuntMarker) {

                viewModel.navigateToHuntMarkerFragment(huntMarker, binding.root.findNavController())

            }
        })
        huntMarkerListView.adapter = adapter
    }

    private fun setupHuntRewards(huntRewards: ArrayList<HuntReward>) {
        huntRewardListView.layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
        huntRewardListView.addItemDecoration(dividerItemDecoration)

        val adapter = HuntRewardsAdapter(huntRewards, object : HuntRewardsAdapter.OnHuntRewardItemClickListener {
            override fun onHuntRewardItemClick(huntReward: HuntReward) {

                viewModel.navigateToHuntRewardFragment(huntReward, binding.root.findNavController())
            }
        })
        huntRewardListView.adapter = adapter
    }



}