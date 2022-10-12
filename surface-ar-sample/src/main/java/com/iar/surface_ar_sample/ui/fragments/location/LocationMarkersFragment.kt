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
class LocationMarkersFragment : BaseFragment() {

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

        val defaultLocation = getString(R.string.default_location)
        binding.markerLocation.text = defaultLocation

        if (DebugSettingsController.simulatedLocation) {
            val coordinateString = "${getString(R.string.coordinates)} ${viewModel.getCoordinates()} ${getString(R.string.radius)}"
            binding.markerLocation.text = coordinateString
            viewModel.onGetLocationMarkers(viewModel.getCoordinates())
        }

        viewModel.locationMarkers.observe(viewLifecycleOwner) { markers ->
            markers?.let {
                setupMarkersList(markers)
            }
        }

        binding.getMarkerButton.setOnClickListener {
            setupDialog()
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Utils.showToastMessage("${getString(R.string.there_is_error)} $error", requireContext())
            }
        }

        binding.downloadOverlay.setOnClickListener {
            viewModel.cancelDownloads()
            binding.downloadOverlay.visibility = View.GONE
        }

        return binding.root
    }

    private fun setupMarkersList(markers: List<Marker>) {
        markerListView.layoutManager = LinearLayoutManager(requireContext())
        context?.let { curContext ->
            markerListView.addDivider(curContext, R.color.lightGrey)

            val adapter =
                LocationMarkersAdapter(
                    markers,
                    object : LocationMarkersAdapter.OnLocationMarkerItemClickListener {
                        override fun onMarkerItemClick(marker: Marker) {
                            (activity as? MainActivity)?.let {

                                viewModel.navigateLocationToSurfaceAR(it, marker) { progress ->
                                    // OnComplete callback.
                                    showDownloadProgress(progress)
                                }
                            }
                        }
                    },
                    object : LocationMarkersAdapter.OnTakeMeThereClickListener {
                        override fun onTakeMeThereClick(marker: Marker) {
                            val lat = String.format("%.6f", marker.location.latitude)
                            val long = String.format("%.6f", marker.location.longitude)
                            val markerLocation =
                                "${marker.location.latitude},${marker.location.longitude}"
                            val locationString = "${getString(R.string.coordinates)} $lat,$long ${getString(R.string.radius)}"

                            viewModel.onGetLocationMarkers(markerLocation)
                            binding.markerLocation.text = locationString
                        }
                    })

            markerListView.adapter = adapter
        }

    }

    private fun setupDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.get_location_markers))
        val container = FrameLayout(requireActivity())
        val editText: EditText = Utils.setupDialogEditText(requireContext())
        editText.setTextIsSelectable(true)
        editText.hint = getString(R.string.location_hint)
        viewModel.editTextFilters(editText)
        container.addView(editText)
        builder.setView(container)
        builder.setMessage(getString(R.string.enter_location_coordinates))
        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
            val inputId = editText.text.toString()
            viewModel.onGetLocationMarkers(inputId)
            val locationString = "${getString(R.string.coordinates)} $inputId ${getString(R.string.radius)}"
            if (viewModel.isValidCoordinates) {
                binding.markerLocation.text = locationString
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.create().show()
    }

    private fun showDownloadProgress(progress: Int) {
        Handler(Looper.getMainLooper()).post {
            if (progress in 0..99) {
                binding.downloadOverlay.visibility = View.VISIBLE
                val progressPercent = "$progress${getString(R.string.percent)}"
                binding.progressText.text = progressPercent
            } else {
                binding.downloadOverlay.visibility = View.GONE
            }
        }
    }

}