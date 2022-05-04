package com.iar.surface_ar_sample.ui.fragments.nfc

import android.os.Bundle
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
                            val readMessage = "Read NFC successfully,  $tag"
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
                Utils.showToastMessage("Could not find the marker", requireContext())
                return@setOnClickListener
            }
            (activity as MainActivity).let { activity ->
                binding.downloadOverlay.visibility = View.VISIBLE

                nfcViewModel.navigateNFCToSurfaceAR(activity, currentMarker)
                nfcViewModel.isComplete.observe(viewLifecycleOwner) { isComplete ->
                    if (isComplete) {
                        binding.downloadOverlay.visibility = View.GONE

                    }
                }
            }

            nfcViewModel.currentMarker = null
        }

        nfcViewModel.error.observe(viewLifecycleOwner, { error ->
            error?.let {
                Utils.showToastMessage("There is error $error", requireContext())
            }
        })
        return binding.root
    }


}