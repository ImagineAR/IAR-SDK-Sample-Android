package com.iar.surface_ar_sample.ui.fragments.ondemand


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.common.Utils
import com.iar.common.Utils.addDivider
import com.iar.iar_core.Marker
import com.iar.surface_ar_sample.R
import com.iar.surface_ar_sample.databinding.FragmentOnDemandMarkersBinding
import com.iar.surface_ar_sample.ui.activities.MainActivity
import com.iar.surface_ar_sample.ui.common.BaseFragment
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnDemandMarkersFragment : BaseFragment() {
    private val viewModel by viewModels<OnDemandMarkersViewModel>()

    private lateinit var binding: FragmentOnDemandMarkersBinding
    private lateinit var markerListView: RecyclerView
    private val args by navArgs<OnDemandMarkersFragmentArgs>()

    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentOnDemandMarkersBinding.inflate(inflater, container, false)
        markerListView = binding.onDemandMarkerList

        viewModel.initialize(requireContext())
        viewModel.getOnDemandMarkers()

        viewModel.onDemandMarkers.observe(viewLifecycleOwner) { markers ->
            markers?.let {
                setupMarkersList(markers)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                context?.let { curContext ->
                    Toast.makeText(curContext, "There is error $it", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.getMarkerButton.setOnClickListener {
            setupDialog()
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
                MarkersAdapter(markers, object : MarkersAdapter.OnMarkerItemClickListener {
                    override fun onMarkerItemClick(marker: Marker) {
                        if (args.isNfc) {
                            val id = marker.id
                            viewModel.navigateToWriteNFCFragment(id)
                            return
                        }

                        (activity as? MainActivity)?.let {
                            viewModel.navigateOnDemandToSurfaceAR(it, marker) { progress ->
                                // OnComplete callback.
                                showDownloadProgress(progress)
                            }
                        }
                    }
                })

            markerListView.adapter = adapter
        }
    }

    private fun setupDialog() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Get Marker by ID")
        val container = FrameLayout(requireActivity())
        val editText: EditText = Utils.setupDialogEditText(requireContext())
        container.addView(editText)
        builder.setView(container)
        builder.setMessage("Enter Marker ID:")
        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
            val inputId = editText.text.toString()

            (activity as? MainActivity)?.let {

                viewModel.getMarkerById(it, inputId) { progress ->
                    // OnComplete callback.
                    showDownloadProgress(progress)
                    viewModel.isValidMarker = false
                }

                if (!viewModel.isValidMarker) {
                    binding.downloadOverlay.visibility = View.GONE
                }

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
                val progressPercent = "$progress%"
                binding.progressText.text = progressPercent
            } else {
                binding.downloadOverlay.visibility = View.GONE
            }
        }
    }

}