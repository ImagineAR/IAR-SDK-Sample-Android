package com.iar.surface_ar_sample.ui.fragments.markers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.iar.surface_ar_sample.databinding.LocationMarkersFragmentBinding
import com.iar.surface_ar_sample.ui.common.BaseFragment
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationMarkersFragment : BaseFragment(){

    private val viewModel by viewModels<MarkersViewModel>()
    override fun getViewModel(): BaseViewModel = viewModel

    private lateinit var binding: LocationMarkersFragmentBinding
    private lateinit var markerListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.validateLicense(requireContext())
        viewModel.getLocationMarkers(48.166667, -100.166667)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LocationMarkersFragmentBinding.inflate(inflater, container, false)
        markerListView = binding.locationMarkerList

        viewModel.locationMarkers.observe(viewLifecycleOwner, { markers ->
            markers?.let {
              //  setupMarkersList(markers)
            }

        })

        binding.getMarkerButton.setOnClickListener {
          //  setupDialog()
        }

        viewModel.error.observe(viewLifecycleOwner, { error ->
            error?.let {
             //   Util.showToastMessage("There is error $error", requireContext())
            }
        })
        return binding.root
    }



}