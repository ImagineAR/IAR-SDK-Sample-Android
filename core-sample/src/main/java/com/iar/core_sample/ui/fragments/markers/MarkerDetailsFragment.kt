package com.iar.core_sample.ui.fragments.markers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.gson.GsonBuilder
import com.iar.core_sample.R
import com.iar.core_sample.databinding.FragmentMarkerDetailsBinding
import com.iar.core_sample.databinding.RewardDetailsFragmentBinding
import com.iar.core_sample.ui.fragments.rewards.RewardDetailsFragmentArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkerDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMarkerDetailsBinding
    private val args by navArgs<MarkerDetailsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentMarkerDetailsBinding.inflate(inflater, container, false)

        val marker = args.marker
        val gson = GsonBuilder().setPrettyPrinting().create()
         val prettyJsonString = gson.toJson(marker)
        binding.markerData.text = prettyJsonString
        return binding.root
    }

}