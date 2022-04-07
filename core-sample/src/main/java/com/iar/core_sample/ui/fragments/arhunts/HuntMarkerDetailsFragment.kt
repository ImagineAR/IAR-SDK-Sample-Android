package com.iar.core_sample.ui.fragments.arhunts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.gson.GsonBuilder
import com.iar.common.Utils.gson
import com.iar.common.Utils.loadImage
import com.iar.core_sample.databinding.FragmentHuntMarkersBinding
import com.iar.iar_core.HuntMarker

class HuntMarkerDetailsFragment : Fragment() {

    private lateinit var binding: FragmentHuntMarkersBinding
    private val args by navArgs<HuntMarkerDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHuntMarkersBinding.inflate(inflater, container, false)

        val huntMarkerString = args.huntMarker
        val huntMarker = gson.fromJson(huntMarkerString, HuntMarker::class.java)

        huntMarker.marker?.previewImageUrl?.let {
            binding.markerImage.loadImage(it, requireContext())
        }
        binding.markerName.text = huntMarker.marker?.name
        binding.markerId.text = huntMarker.marker?.id

        val gson = GsonBuilder().setPrettyPrinting().create()
        val huntMarkerData = gson.toJson(huntMarker)
        binding.huntMarkerData.text = huntMarkerData

        return binding.root
    }

}