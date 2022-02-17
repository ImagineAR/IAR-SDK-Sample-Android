package com.iar.surface_ar_sample.ui.fragments.nfc


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
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
    private val nfcViewModel by activityViewModels<NFCViewModel>()
    private var isWrite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWriteNfcBinding.inflate(inflater, container, false)
        val currentActivity = activity as? MainActivity

        nfcViewModel.nfcController.observe(viewLifecycleOwner) { controller ->
            controller?.let {
                nfcController = controller
            }
        }

        binding.writeButton.setOnClickListener { view ->
            nfcController?.let {
                nfcViewModel.startNfc(it, true)
                markerId = binding.markerInput.editText?.text.toString()
                nfcViewModel.closeKeyBoard(view, currentActivity as AppCompatActivity)
            }
        }

        nfcViewModel.isWrite.observe(viewLifecycleOwner) { write ->
            isWrite = write
        }

        nfcViewModel.currentIntent.observe(viewLifecycleOwner) { intent ->
            nfcController?.let {
                if (isWrite) {
                    val writeMessage = nfcViewModel.writeNfc(markerId, it, intent)
                    binding.writeMessage.text = writeMessage

                }
            }
        }

        binding.randomizeButton.setOnClickListener {
            nfcController?.let {
                nfcViewModel.startNfc(it, true)
                markerId = UUID.randomUUID().toString()
                binding.markerInput.editText?.setText(markerId)
            }
        }

        return binding.root
    }

}