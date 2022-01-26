package com.iar.core_sample.ui.fragments.markers


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.core_sample.databinding.OnDemandMarkersFragmentBinding
import com.iar.core_sample.utils.Util
import com.iar.core_sample.utils.Util.addDivider
import com.iar.iar_core.Marker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnDemandMarkersFragment : Fragment() {
    private val viewModel by viewModels<MarkersViewModel>()

    private lateinit var binding: OnDemandMarkersFragmentBinding
    private lateinit var markerListView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = OnDemandMarkersFragmentBinding.inflate(inflater, container, false)
        markerListView = binding.onDemandMarkerList
        viewModel.initialize(requireContext())

        viewModel.userId.observe(viewLifecycleOwner) { userId ->
            userId?.let { viewModel.getOnDemandMarkers() }
        }

        viewModel.onDemandMarkers.observe(viewLifecycleOwner) { markers ->
            markers?.let {
                setupMarkersList(markers)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Util.showToastMessage("There is error $error", requireContext())
            }
        }
        return binding.root
    }

    private fun setupMarkersList(markers: List<Marker>) {
        markerListView.layoutManager = LinearLayoutManager(requireContext())

        markerListView.addDivider(requireContext())
        val adapter =
            MarkersAdapter(markers, object : MarkersAdapter.OnMarkerItemClickListener {
                override fun onMarkerItemClick(marker: Marker) {
                    viewModel.navigateOnDemandToMarkerDetailsFragment(marker)
                }
            })

        markerListView.adapter = adapter

    }

}