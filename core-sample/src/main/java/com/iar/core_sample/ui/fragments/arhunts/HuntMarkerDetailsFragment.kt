package com.iar.core_sample.ui.fragments.arhunts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.iar.core_sample.databinding.FragmentHuntMarkersBinding
import com.iar.core_sample.utils.Util
import com.iar.core_sample.utils.Util.loadImage
import com.iar.iar_core.HuntMarker

class HuntMarkersFragment : Fragment() {

    private lateinit var binding: FragmentHuntMarkersBinding
    private val args by navArgs<HuntMarkersFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHuntMarkersBinding.inflate(inflater, container, false)

        val huntMarkerString = args.huntMarker
        val huntMarker = Util.gson.fromJson(huntMarkerString, HuntMarker::class.java)

        huntMarker.clueCard?.imageUrl?.let {
            binding.clueCardImage.loadImage(it, requireContext())
        }
        binding.clueCardId.text = huntMarker.clueCard?.id
        binding.clueCardDescription.text = huntMarker.clueCard?.description

        val huntMarkerData = "ID: ${huntMarker.id}\n" +
                "HuntID: ${huntMarker.huntId}\n" +
                "MarkerID: ${huntMarker.markerId}\n" +
                "Scanned: ${huntMarker.scanned}\n" +
                "Marker: ${Util.gson.toJson(huntMarker.marker)}"
        binding.huntMarkerData.text = huntMarkerData

        return binding.root
    }

}