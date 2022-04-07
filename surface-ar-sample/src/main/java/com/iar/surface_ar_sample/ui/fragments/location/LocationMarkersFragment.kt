package com.iar.surface_ar_sample.ui.fragments.location

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.common.Utils
import com.iar.common.Utils.addDivider
import com.iar.iar_core.Marker
import com.iar.iar_core.controllers.DebugSettingsController
import com.iar.surface_ar_sample.R
import com.iar.surface_ar_sample.databinding.LocationMarkersFragmentBinding
import com.iar.surface_ar_sample.ui.activities.MainActivity

import com.iar.surface_ar_sample.ui.common.BaseFragment
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationMarkersFragment : BaseFragment(){

    private val viewModel by viewModels<LocationMarkersViewModel>()
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

        val defaultLocation = "Coordinates: 48.166667,-100.166667 Radius: 10000"
        binding.markerLocation.text = defaultLocation

        if(DebugSettingsController.simulatedLocation){
            val coordinateString = "Coordinates: ${viewModel.getCoordinates()} Radius: 10000"
            binding.markerLocation.text = coordinateString
            viewModel.onGetLocationMarkers(viewModel.getCoordinates())
        }

        viewModel.locationMarkers.observe(viewLifecycleOwner, { markers ->
            markers?.let {
                setupMarkersList(markers)
            }

        })

        binding.getMarkerButton.setOnClickListener {
            setupDialog()
        }

        viewModel.error.observe(viewLifecycleOwner, { error ->
            error?.let {
                Utils.showToastMessage("There is error $error", requireContext())
            }
        })
        return binding.root
    }

    private fun setupMarkersList(markers: List<Marker>) {
        markerListView.layoutManager = LinearLayoutManager(requireContext())
        context?.let { curContext ->
            markerListView.addDivider(curContext, R.color.lightGrey)

            val adapter =
                LocationMakersAdapter(
                    markers,
                    object : LocationMakersAdapter.OnLocationMarkerItemClickListener {
                        override fun onMarkerItemClick(marker: Marker) {
                            (activity as? MainActivity)?.let {
                                binding.downloadOverlay.visibility = View.VISIBLE

                                viewModel.navigateLocationToSurfaceAR(it, marker) {
                                    // OnComplete callback.
                                    Handler(Looper.getMainLooper()).post {
                                        binding.downloadOverlay.visibility = View.GONE
                                    }
                                }
                            }
                        }
                    },
                    object : LocationMakersAdapter.OnTakeMeThereClickListener {
                        override fun onTakeMeThereClick(marker: Marker) {
                            val lat = String.format("%.6f", marker.location.latitude)
                            val long= String.format("%.6f", marker.location.longitude)
                            val markerLocation = "${marker.location.latitude},${marker.location.longitude}"
                            val locationString = "Coordinates: $lat,$long Radius: 10000"

                            viewModel.onGetLocationMarkers(markerLocation)
                            binding.markerLocation.text = locationString
                        }
                    })

            markerListView.adapter = adapter
        }

    }

    private fun setupDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Get Location Markers")
        val container = FrameLayout(requireActivity())
        val editText: EditText = Utils.setupDialogEditText(requireContext())
        editText.setTextIsSelectable(true)
        editText.hint = "48.166667, -100.166667"
        viewModel.editTextFilters(editText)
        container.addView(editText)
        builder.setView(container)
        builder.setMessage("Enter location coordinates")
        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
            val inputId = editText.text.toString()
            viewModel.onGetLocationMarkers(inputId)
            val locationString = "Coordinates: $inputId Radius: 10000"
            if (viewModel.isValidCoordinates) {
                binding.markerLocation.text = locationString
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.create().show()
    }

}