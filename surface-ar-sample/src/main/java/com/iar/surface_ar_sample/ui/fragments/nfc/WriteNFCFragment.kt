package com.iar.surface_ar_sample.ui.fragments.nfc


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iar.nfc_sdk.NFCController
import com.iar.surface_ar_sample.databinding.FragmentWriteNfcBinding
import com.iar.surface_ar_sample.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class WriteNFCFragment : Fragment() {

    private lateinit var binding: FragmentWriteNfcBinding
    private var nfcController: NFCController? = null
    var markerId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWriteNfcBinding.inflate(inflater, container, false)
        val currentActivity = activity as? MainActivity

        currentActivity?.nfcViewModel?.nfcController?.observe(viewLifecycleOwner) { controller ->
            controller?.let {
                nfcController = controller
            }
        }
        binding.writeButton.setOnClickListener { view ->
            nfcController?.let {
                currentActivity?.nfcViewModel?.startNfc(it)
                markerId = binding.markerInput.editText?.text.toString()
                currentActivity?.nfcViewModel?.closeKeyBoard(view, currentActivity)
            }

        }

        currentActivity?.nfcViewModel?.currentIntent?.observe(viewLifecycleOwner) { intent ->
            nfcController?.let {

                val writeMessage = currentActivity.nfcViewModel.writeNfc(markerId, it, intent)

                binding.writeMessage.text = writeMessage
            }
        }

        binding.randomizeButton.setOnClickListener {
            nfcController?.let {
                currentActivity?.nfcViewModel?.startNfc(it)
                markerId = UUID.randomUUID().toString()
                binding.markerInput.editText?.setText(markerId)
            }
        }

        return binding.root
    }

}