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
import com.iar.core_sample.databinding.OnDemandMarkersFragmentBinding
import com.iar.core_sample.ui.common.BaseFragment
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.core_sample.utils.Util
import com.iar.iar_core.Marker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnDemandMarkersFragment : BaseFragment() {
    private val viewModel by viewModels<MarkersViewModel>()

    private lateinit var binding: OnDemandMarkersFragmentBinding
    private lateinit var markerListView: RecyclerView
    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = OnDemandMarkersFragmentBinding.inflate(inflater, container, false)
        markerListView = binding.onDemandMarkerList
        viewModel.initialize(requireContext())

        viewModel.userId.observe(viewLifecycleOwner) { userId ->
            userId?.let { viewModel.getOnDemandMarkers() }
        }

        viewModel.onDemandMarkers.observe(viewLifecycleOwner) { markers ->
            markers?.let {
                setupMarkersList(markers)
            }
        }

        binding.getMarkerButton.setOnClickListener {
            setupDialog()
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showToastMessage("There is error $error", requireContext())
            }
        }
        return binding.root
    }

    private fun setupMarkersList(markers: List<Marker>) {
        markerListView.layoutManager = LinearLayoutManager(requireContext())

        markerListView.addDivider(requireContext(), R.color.lightGrey)
        val adapter =
            MarkersAdapter(markers, object : MarkersAdapter.OnMarkerItemClickListener {
                override fun onMarkerItemClick(marker: Marker) {
                    viewModel.navigateOnDemandToMarkerDetailsFragment(marker)
                }
            },
                object : MarkersAdapter.OnTakeMeThereClickListener {
                    override fun onTakeMeThereClick(marker: Marker) {

                    }
                })

        markerListView.adapter = adapter

    }

    private fun setupDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Get Marker by ID")
        val container = FrameLayout(requireActivity())
        val editText: EditText = Util.setupDialogEditText(requireContext())
        container.addView(editText)
        builder.setView(container)
        builder.setMessage("Enter Marker ID:")
        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
            val inputId = editText.text.toString()
            viewModel.getMarkerById(inputId)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.create().show()
    }

}