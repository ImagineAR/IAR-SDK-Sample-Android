package com.iar.surface_ar_sample.ui.fragments.nfc

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReadNfcBinding.inflate(inflater,container, false)

        val currentActivity = activity as? MainActivity

        currentActivity?.nfcViewModel?.nfcController?.observe(viewLifecycleOwner) { controller ->
            controller?.let {
                nfcController = controller
            }
        }

        binding.readButton.setOnClickListener {
            nfcController?.let {
                currentActivity?.nfcViewModel?.startNfc(it)
            }
        }

        currentActivity?.nfcViewModel?.currentIntent?.observe(viewLifecycleOwner) {intent ->
            nfcController?.let {
                val markerTag = currentActivity.nfcViewModel.readNfc(it, intent)
                markerTag?.let{ tag ->
                 val   readMessage = "Read NFC successfully,  $tag"
                    binding.readMessage.text = readMessage
                    currentActivity.nfcViewModel.getMarkerById(tag.id)
                }
            }
        }

        currentActivity?.nfcViewModel?.marker?.observe(viewLifecycleOwner){marker ->

            marker?.let{
                nfcMarker = it
                println(it.id)
            }
        }

        binding.markerButton.setOnClickListener {

            nfcMarker?.let{
                 currentActivity?.nfcViewModel?.navigateNFCToSurfaceAR(currentActivity, it)

            }
        }

        return binding.root
    }


}