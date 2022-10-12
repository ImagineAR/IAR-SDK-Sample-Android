package com.iar.surface_ar_sample.ui.fragments.nfc

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.iar.common.Utils
import com.iar.nfc_sdk.NFCController
import com.iar.surface_ar_sample.R
import com.iar.surface_ar_sample.databinding.FragmentReadNfcBinding
import com.iar.surface_ar_sample.ui.activities.MainActivity
import com.iar.surface_ar_sample.ui.common.BaseFragment
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadNFCFragment : BaseFragment() {
    private lateinit var binding: FragmentReadNfcBinding
    private var nfcController: NFCController? = null
    private val nfcViewModel by activityViewModels<NFCViewModel>()

    private var isWrite: Boolean = true
    override fun getViewModel(): BaseViewModel = nfcViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReadNfcBinding.inflate(inflater, container, false)

        nfcViewModel.validateLicense(requireContext())

        val currentActivity = activity as? MainActivity

        if (Utils.checkNFCSupported(requireContext())) {
            nfcViewModel.nfcController.observe(viewLifecycleOwner) { controller ->
                controller?.let {
                    nfcController = controller
                }
            }
        }

        binding.readButton.setOnClickListener {
            if (Utils.checkNFCSupported(requireContext())) {
                nfcController?.let {
                    currentActivity?.nfcViewModel?.startNfc(it, false)
                }
            } else {
                Utils.showToastMessage(getString(R.string.nfc_upsupport), requireContext())
            }
        }

        nfcViewModel.isWrite.observe(viewLifecycleOwner) { write ->
            isWrite = write
        }

        if (Utils.checkNFCSupported(requireContext())) {
            nfcViewModel.currentIntent.observe(viewLifecycleOwner) { intent ->
                nfcController?.let { controller ->
                    if (!isWrite) {
                        val markerTag = nfcViewModel.readNfc(controller, intent)
                        markerTag?.let { tag ->
                            val readMessage = "${getString(R.string.read_nfc_success)}  $tag"
                            binding.readMessage.text = readMessage
                            nfcViewModel.getMarkerById(tag.id)
                            nfcViewModel.setIsWrite(true)
                        }
                    }
                }
            }
        }

        binding.markerButton.setOnClickListener {

            val currentMarker = nfcViewModel.currentMarker

            if (currentMarker == null) {
                Utils.showToastMessage(getString(R.string.not_find_marker), requireContext())
                return@setOnClickListener
            }
            (activity as MainActivity).let { activity ->
                nfcViewModel.navigateNFCToSurfaceAR(activity, currentMarker) { progress ->
                    showDownloadProgress(progress)
                }
            }

            nfcViewModel.currentMarker = null
        }

        nfcViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Utils.showToastMessage("${getString(R.string.there_is_error)} $error", requireContext())
            }
        }

        binding.downloadOverlay.setOnClickListener {
            nfcViewModel.cancelDownloads()
            binding.downloadOverlay.visibility = View.GONE
        }

        return binding.root
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