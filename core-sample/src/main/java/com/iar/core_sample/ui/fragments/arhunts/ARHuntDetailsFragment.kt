package com.iar.core_sample.ui.fragments.arhunts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.common.Utils.addDivider
import com.iar.common.Utils.gson
import com.iar.common.Utils.loadImage
import com.iar.core_sample.R
import com.iar.core_sample.databinding.FragmentHuntDetailsBinding
import com.iar.core_sample.ui.common.BaseFragment
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.iar_core.Hunt
import com.iar.iar_core.HuntMarker
import com.iar.iar_core.HuntReward
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ARHuntDetailsFragment : BaseFragment() {

    private val viewModel by viewModels<ARHuntsViewModel>()


    private lateinit var binding: FragmentHuntDetailsBinding
    private val args by navArgs<ARHuntDetailsFragmentArgs>()
    private lateinit var huntMarkerListView: RecyclerView
    private lateinit var huntRewardListView: RecyclerView
    override fun getViewModel(): BaseViewModel = viewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHuntDetailsBinding.inflate(inflater, container, false)
        val huntString = args.hunt
        val hunt = gson.fromJson(huntString, Hunt::class.java)

        binding.huntName.text = hunt.name
        binding.huntId.text = hunt.id
        binding.huntDescription.text = hunt.description
        binding.huntImage.loadImage(hunt.thumbnailUrl, requireContext())

        val huntData = "${getString(R.string.start_date)} ${hunt.startDate}\n" +
                "${getString(R.string.end_date)} ${hunt.endDate}\n" +
                "${getString(R.string.retroactive_contribution)} ${hunt.retroactiveContribution}\n"
        binding.huntData.text = huntData

        huntMarkerListView = binding.huntMarkerList

        val huntMarkers = hunt.huntMarkers
        huntMarkers?.let {
            setupHuntMarkers(it)
        }

        huntRewardListView = binding.huntRewardList

        val huntRewards = hunt.huntRewards
        huntRewards?.let {
            setupHuntRewards(it)
        }

        return binding.root
    }

    private fun setupHuntMarkers(huntMarkers: ArrayList<HuntMarker>) {
        huntMarkerListView.layoutManager = LinearLayoutManager(requireContext())

        huntMarkerListView.addDivider(requireContext(), R.color.lightGrey)

        val adapter = HuntMarkersAdapter(
            huntMarkers,
            object : HuntMarkersAdapter.OnHuntMarkerItemClickListener {
                override fun onHuntMarkerItemClick(huntMarker: HuntMarker) {

                    viewModel.navigateToHuntMarkerFragment(
                        huntMarker
                    )

                }
            })
        huntMarkerListView.adapter = adapter
    }

    private fun setupHuntRewards(huntRewards: ArrayList<HuntReward>) {
        huntRewardListView.layoutManager = LinearLayoutManager(requireContext())

        huntRewardListView.addDivider(requireContext(), R.color.lightGrey)

        val adapter = HuntRewardsAdapter(
            huntRewards,
            object : HuntRewardsAdapter.OnHuntRewardItemClickListener {
                override fun onHuntRewardItemClick(huntReward: HuntReward) {

                    viewModel.navigateToHuntRewardFragment(
                        huntReward
                    )
                }
            })
        huntRewardListView.adapter = adapter
    }
}