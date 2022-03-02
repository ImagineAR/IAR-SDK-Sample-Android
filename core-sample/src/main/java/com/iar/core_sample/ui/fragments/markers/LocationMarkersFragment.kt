package com.iar.core_sample.ui.fragments.markers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.common.Utils.addDivider
import com.iar.common.Utils.showToastMessage
import com.iar.core_sample.R
import com.iar.core_sample.databinding.FragmentLocationMarkersBinding
import com.iar.core_sample.ui.common.BaseFragment
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.core_sample.utils.Util.setupDialogEditText
import com.iar.iar_core.Marker
import com.iar.iar_core.controllers.DebugSettingsController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationMarkersFragment : BaseFragment() {
    private val viewModel by viewModels<MarkersViewModel>()

    private lateinit var binding: FragmentLocationMarkersBinding
    private lateinit var markerListView: RecyclerView
    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.validateLicense(requireContext())
        viewModel.getLocationMarkers(48.166667, -100.166667)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLocationMarkersBinding.inflate(inflater, container, false)
        markerListView = binding.locationMarkerList
        val defaultLocation = "Coordinates: 48.166667,-100.166667 Radius: 10000"
        binding.markerLocation.text = defaultLocation

        if(DebugSettingsController.simulatedLocation){
            val coordinateString = "Coordinates: ${viewModel.getCoordinates()} Radius: 10000"
            binding.markerLocation.text = coordinateString
            viewModel.onGetLocationMarkers(viewModel.getCoordinates())
        }

        viewModel.locationMarkers.observe(viewLifecycleOwner) { markers ->
            markers?.let {
                setupMarkersList(markers)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showToastMessage("There is error $error", requireContext())
            }
        }

        binding.getMarkerButton.setOnClickListener {
            setupDialog()
        }

        return binding.root
    }

    private fun setupMarkersList(markers: List<Marker>) {
        markerListView.layoutManager = LinearLayoutManager(requireContext())

        markerListView.addDivider(requireContext(), R.color.lightGrey)
        val adapter =
            MarkersAdapter(markers, object : MarkersAdapter.OnMarkerItemClickListener {
                override fun onMarkerItemClick(marker: Marker) {
                    viewModel.navigateLocationToMarkerDetailsFragment(
                        marker
                    )
                }
            },
                object : MarkersAdapter.OnTakeMeThereClickListener {
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

    private fun setupDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Get Location Markers")
        val container = FrameLayout(requireActivity())
        val editText: EditText = setupDialogEditText(requireContext())
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
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
        builder.create().show()
    }

}