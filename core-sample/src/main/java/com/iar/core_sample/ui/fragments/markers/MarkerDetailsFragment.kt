package com.iar.core_sample.ui.fragments.markers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iar.core_sample.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkerDetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_marker_details, container, false)
    }


}