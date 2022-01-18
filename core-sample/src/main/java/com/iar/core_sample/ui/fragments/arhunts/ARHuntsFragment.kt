package com.iar.core_sample.ui.fragments.arhunts


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.core_sample.R
import com.iar.core_sample.databinding.ArHuntsFragmentBinding
import com.iar.core_sample.utils.Util
import com.iar.iar_core.Hunt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ARHuntsFragment : Fragment() {

    private val viewModel by viewModels<ARHuntsViewModel>()

    private lateinit var binding: ArHuntsFragmentBinding
    private lateinit var huntListView: RecyclerView
    private var huntList: ArrayList<Hunt>? = null
    private var currentUserId: String? = null
    private var errorMessage: String? = null
    private var singleHunt: Hunt? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        println("onCreateView called")

        binding = ArHuntsFragmentBinding.inflate(inflater, container, false)
        huntListView = binding.huntList

        viewModel.initialize(requireContext())

        viewModel.userId.observe(viewLifecycleOwner, { userId ->
            currentUserId = userId
            userId?.let {
                viewModel.getARHunts()
            }
        })

        viewModel.arHunts.observe(viewLifecycleOwner, { hunts ->
            setupHunts(hunts)
            huntList = hunts
        })

        viewModel.arSingleHunt.observe(viewLifecycleOwner, { hunt ->
            singleHunt = hunt
        })


        viewModel.error.observe(viewLifecycleOwner, { error ->
            error?.let {
                showToastMessage(error)
                viewModel.clearErrorMessage()
            }
            errorMessage = error
        })

        binding.getHuntButton.setOnClickListener {
            setupDialog()
        }

        return binding.root
    }


    private fun setupHunts(hunts: ArrayList<Hunt>) {
        huntListView.layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        )
        huntListView.addItemDecoration(dividerItemDecoration)

        val adapter = ARHuntsAdapter(hunts, object : ARHuntsAdapter.OnHuntItemClickListener {
            override fun onHuntItemClick(hunt: Hunt) {
                // navigateToHuntDetailsFragment(hunt)
                viewModel.navigateToARHuntDetailsFragment(hunt, binding.root.findNavController())
            }
        })
        huntListView.adapter = adapter
    }

    private fun setupDialog() {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle("Get AR Hunt")
        val container = FrameLayout(requireActivity())
        val editText: EditText = Util.setupDialogEditText(requireContext())
        container.addView(editText)
        builder.setView(container)
        builder.setMessage("Enter hunt ID")
        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
            val inputId = editText.text.toString()

            getSingleHunt(inputId)
            onGetARHunt()
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.create().show()
    }

    private fun showToastMessage(message: String) {
        val toast = Toast.makeText(
            requireActivity().getApplicationContext(),
            message,
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

   private fun getSingleHunt(inputId: String){

       if (currentUserId != null) {
           viewModel.getSingleHunt(inputId)


       } else {
           showToastMessage("Don't have the user Id")
       }

   }

    private fun onGetARHunt(){

        huntList?.let {
            viewModel.arSingleHunt.observe(viewLifecycleOwner, { hunt ->
                hunt?.let {

                    viewModel.navigateToARHuntDetailsFragment(
                        it,
                        binding.root.findNavController()
                    )
                    viewModel.setSingleHuntNull()
                }


            })

        }
    }


}