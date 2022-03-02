package com.iar.core_sample.ui.fragments.markers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.gson.GsonBuilder
import com.iar.common.Utils.loadImage
import com.iar.core_sample.databinding.FragmentMarkerDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkerDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMarkerDetailsBinding
    private val args by navArgs<MarkerDetailsFragmentArgs>()
    private val viewModel by viewModels<MarkersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val marker = args.marker
        viewModel.initialize(requireContext())
        viewModel.getMarkerDetail(marker.id)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarkerDetailsBinding.inflate(inflater, container, false)

        val marker = args.marker

        marker.previewImageUrl?.let {
            binding.markerImage.loadImage(it, requireContext())
        }
        binding.markerName.text = marker.name
        binding.markerId.text = marker.id

        viewModel.marker.observe(viewLifecycleOwner) {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val prettyJsonString = gson.toJson(it)
            binding.markerData.text = prettyJsonString
        }

        return binding.root
    }

}