package com.iar.surface_ar_sample.ui.fragments.nfc


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.iar.common.Utils
import com.iar.nfc_sdk.NFCController
import com.iar.surface_ar_sample.R
import com.iar.surface_ar_sample.databinding.FragmentWriteNfcBinding
import com.iar.surface_ar_sample.ui.activities.MainActivity
import com.iar.surface_ar_sample.ui.common.BaseFragment
import com.iar.surface_ar_sample.ui.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class WriteNFCFragment : BaseFragment() {

    private lateinit var binding: FragmentWriteNfcBinding
    private var nfcController: NFCController? = null
    var markerId = ""
    private val nfcViewModel by activityViewModels<NFCViewModel>()
    private var isWrite: Boolean = false

    override fun getViewModel(): BaseViewModel = nfcViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                nfcViewModel.navigateRoot()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWriteNfcBinding.inflate(inflater, container, false)
        val currentActivity = activity as? MainActivity

        val args by navArgs<WriteNFCFragmentArgs>()
        val id = args.markerId
        id?.let {
            binding.markerInput.editText?.setText(it)
        }

        if (Utils.checkNFCSupported(requireContext())) {
            nfcViewModel.nfcController.observe(viewLifecycleOwner) { controller ->
                controller?.let {
                    nfcController = controller
                }
            }
        }

        binding.writeButton.setOnClickListener { view ->
            if (Utils.checkNFCSupported(requireContext())) {
                nfcController?.let {
                    nfcViewModel.startNfc(it, true)
                    markerId = binding.markerInput.editText?.text.toString()
                    nfcViewModel.closeKeyBoard(view, currentActivity as AppCompatActivity)
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
                nfcController?.let {
                    if (isWrite) {
                        val writeMessage = nfcViewModel.writeNfc(markerId, it, intent)
                        binding.writeMessage.text = writeMessage

                    }
                }
            }
        }


        binding.randomizeButton.setOnClickListener {
            if (Utils.checkNFCSupported(requireContext())) {
                nfcController?.let {
                    nfcViewModel.startNfc(it, true)
                    markerId = UUID.randomUUID().toString()
                    binding.markerInput.editText?.setText(markerId)
                }
            } else {
                Utils.showToastMessage(getString(R.string.nfc_upsupport), requireContext())
            }
        }

        binding.pickButton.setOnClickListener {
            nfcViewModel.navigateToOnDemandMarkersFragment(true)
        }

        return binding.root
    }

}