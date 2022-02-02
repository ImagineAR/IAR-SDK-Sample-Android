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

class HuntMarkersFragment : Fragment() {

    private lateinit var binding: FragmentHuntMarkersBinding
    private val args by navArgs<HuntMarkersFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHuntMarkersBinding.inflate(inflater, container, false)

        val huntMarkerString = args.huntMarker
        val huntMarker = gson.fromJson(huntMarkerString, HuntMarker::class.java)

        huntMarker.clueCard?.imageUrl?.let {
            binding.clueCardImage.loadImage(it, requireContext())
        }
        binding.clueCardId.text = huntMarker.clueCard?.id
        binding.clueCardDescription.text = huntMarker.clueCard?.description

        val gson = GsonBuilder().setPrettyPrinting().create()
        val huntMarkerData = gson.toJson(huntMarker)
        binding.huntMarkerData.text = huntMarkerData

        return binding.root
    }

}