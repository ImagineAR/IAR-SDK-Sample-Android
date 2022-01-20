package com.iar.core_sample.ui.fragments.markers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.core_sample.R
import com.iar.core_sample.databinding.FragmentLocationMarkersBinding
import com.iar.core_sample.utils.Util
import com.iar.core_sample.utils.Util.addDivider
import com.iar.iar_core.Marker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationMarkersFragment : Fragment() {
    private val viewModel by viewModels<MarkersViewModel>()

    private lateinit var binding: FragmentLocationMarkersBinding
    private lateinit var markerListView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationMarkersBinding.inflate(inflater, container, false)
        markerListView = binding.locationMarkerList

        viewModel.validateLIcense(requireContext())
        viewModel.getLocationMarkers(48.166667, -100.166667)

        viewModel.locationMarkers.observe(viewLifecycleOwner, { markers ->
            markers?.let {
                setupMarkersList(markers)
            }

        })

        viewModel.error.observe(viewLifecycleOwner, { error ->
            error?.let {
                Util.showToastMessage("There is error $error", requireContext())
            }
        })

        binding.getMarkerButton.setOnClickListener {
            setupDialog()
        }

        return binding.root
    }

    private fun setupMarkersList(markers: List<Marker>) {
        markerListView.layoutManager = LinearLayoutManager(requireContext())

        markerListView.addDivider(requireContext())
        val adapter =
            MarkersAdapter(markers, object : MarkersAdapter.OnMarkerItemClickListener {
                override fun onMarkerItemClick(marker: Marker) {
                    viewModel.navigateLocationToMarkerDetailsFragment(
                        marker,
                        binding.root.findNavController()
                    )
                }
            })

        markerListView.adapter = adapter

    }

    private fun setupDialog() {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("Get Location Markers")
        val container = FrameLayout(requireActivity())
        val editText: EditText = Util.setupDialogEditText(requireContext())
        editText.setTextIsSelectable(true)
        editText.hint = "48.166667, -100.166667"
        container.addView(editText)
        builder.setView(container)
        builder.setMessage("Enter location coordinates")
        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
            val inputId = editText.text.toString()
            viewModel.onGetLocationMarkers(inputId,binding.root.findNavController())
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.create().show()
    }

}