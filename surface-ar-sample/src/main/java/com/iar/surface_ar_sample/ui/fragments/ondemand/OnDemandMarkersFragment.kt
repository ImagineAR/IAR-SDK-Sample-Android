package com.iar.surface_ar_sample.ui.fragments.ondemand


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentOnDemandMarkersBinding.inflate(inflater, container, false)
        markerListView = binding.onDemandMarkerList

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
        return binding.root
    }

    private fun setupMarkersList(markers: List<Marker>) {
        markerListView.layoutManager = LinearLayoutManager(requireContext())

        context?.let { curContext ->
            markerListView.addDivider(curContext, R.color.lightGrey)
            val adapter =
                MarkersAdapter(markers, object : MarkersAdapter.OnMarkerItemClickListener {
                    override fun onMarkerItemClick(marker: Marker) {
                        (activity as? MainActivity)?.let {
                            binding.downloadOverlay.visibility = View.VISIBLE

                            viewModel.navigateOnDemandToSurfaceAR(it, marker) {
                                // OnComplete callback.
                                Handler(Looper.getMainLooper()).post {
                                    binding.downloadOverlay.visibility = View.GONE
                                }
                            }
                        }
                    }
                })

            markerListView.adapter = adapter
        }
    }

}