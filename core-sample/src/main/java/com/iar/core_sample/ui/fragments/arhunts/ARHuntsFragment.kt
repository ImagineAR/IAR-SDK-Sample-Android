package com.iar.core_sample.ui.fragments.arhunts


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
import com.iar.core_sample.R
import com.iar.core_sample.databinding.ArHuntsFragmentBinding
import com.iar.core_sample.ui.common.BaseFragment
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.core_sample.utils.Util
import com.iar.core_sample.utils.Util.addDivider
import com.iar.iar_core.Hunt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ARHuntsFragment : BaseFragment() {

    private val viewModel by viewModels<ARHuntsViewModel>()
    private lateinit var binding: ArHuntsFragmentBinding
    private lateinit var huntListView: RecyclerView
    private var huntList: ArrayList<Hunt>? = null
    override fun getViewModel(): BaseViewModel = viewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ArHuntsFragmentBinding.inflate(inflater, container, false)
        huntListView = binding.huntList

        viewModel.initialize(requireContext())

        viewModel.userId.observe(viewLifecycleOwner) { userId ->
            userId?.let {
                viewModel.getARHunts()
            }
        }

        viewModel.arHunts.observe(viewLifecycleOwner) { hunts ->
            setupHunts(hunts)
            huntList = hunts
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Util.showToastMessage(error, requireContext())
            }
        }

        binding.getHuntButton.setOnClickListener {
            setupDialog()
        }

        return binding.root
    }

    private fun setupHunts(hunts: ArrayList<Hunt>) {
        huntListView.layoutManager = LinearLayoutManager(requireContext())

        huntListView.addDivider(requireContext())

        val adapter = ARHuntsAdapter(hunts, object : ARHuntsAdapter.OnHuntItemClickListener {
            override fun onHuntItemClick(hunt: Hunt) {
                viewModel.navigateToARHuntDetailsFragment(hunt)
            }
        })
        huntListView.adapter = adapter
    }

    private fun setupDialog() {
        val builder= AlertDialog.Builder(requireActivity())
        builder.setTitle("Get AR Hunt")
        val container = FrameLayout(requireActivity())
        val editText: EditText = Util.setupDialogEditText(requireContext())
        container.addView(editText)
        builder.setView(container)
        builder.setMessage("Enter hunt ID")

        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
            val inputId = editText.text.toString()
            onGetARHunt(inputId)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.create().show()
    }

    private fun onGetARHunt(inputId: String) {
        huntList?.let {
            val singleHunt = viewModel.getHuntFromId(inputId, it)

            if (singleHunt != null) {
                viewModel.navigateToARHuntDetailsFragment(
                    singleHunt
                )
            } else {
                Util.showToastMessage("Don't have the hunt Id", requireContext())
            }
        }

    }
}