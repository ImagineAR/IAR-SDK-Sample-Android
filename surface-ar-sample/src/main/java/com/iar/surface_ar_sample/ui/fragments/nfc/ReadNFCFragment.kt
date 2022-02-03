package com.iar.surface_ar_sample.ui.fragments.nfc

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.iar.common.Utils
import com.iar.iar_core.Marker
import com.iar.nfc_sdk.NFCController
import com.iar.surface_ar_sample.R
import com.iar.surface_ar_sample.databinding.FragmentReadNfcBinding
import com.iar.surface_ar_sample.databinding.FragmentWriteNfcBinding
import com.iar.surface_ar_sample.ui.activities.MainActivity


class ReadNFCFragment : Fragment() {
    private lateinit var binding: FragmentReadNfcBinding
    private var nfcController: NFCController? = null
    private var nfcMarker: Marker? = null
    private val nfcViewModel by activityViewModels<NFCViewModel>()

    private var isWrite: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReadNfcBinding.inflate(inflater,container, false)

        nfcViewModel.validateLicense(requireContext())

        val currentActivity = activity as? MainActivity

        nfcViewModel.nfcController.observe(viewLifecycleOwner) { controller ->
            controller?.let {
                nfcController = controller
            }
        }

        binding.readButton.setOnClickListener {
            nfcController?.let {
                currentActivity?.nfcViewModel?.startNfc(it, false)
            }
        }

        nfcViewModel.isWrite.observe(viewLifecycleOwner) { write ->
            isWrite = write
        }

        nfcViewModel.currentIntent.observe(viewLifecycleOwner) { intent ->
            nfcController?.let { controller ->
                if (!isWrite) {
                    val markerTag = nfcViewModel.readNfc(controller, intent)
                    markerTag?.let { tag ->
                        val readMessage = "Read NFC successfully,  $tag"
                        binding.readMessage.text = readMessage
                        nfcViewModel.getMarkerById(tag.id)
                    }
                }
            }
        }

        nfcViewModel.marker.observe(viewLifecycleOwner){marker ->
            marker?.let{
                nfcMarker = it
            }
        }

        binding.markerButton.setOnClickListener {

            nfcMarker?.let { marker ->
                println(marker.id)
                currentActivity?.let { activity ->
                    binding.downloadOverlay.visibility = View.VISIBLE
                    nfcViewModel.navigateNFCToSurfaceAR(activity, marker)
                    // OnComplete callback.
                    Handler(Looper.getMainLooper()).post {
                        binding.downloadOverlay.visibility = View.GONE
                    }
                }
            }

        }

        nfcViewModel.error.observe(viewLifecycleOwner, { error ->
            error?.let {
                Utils.showToastMessage("There is error $error", requireContext())
            }
        })
        return binding.root
    }


}