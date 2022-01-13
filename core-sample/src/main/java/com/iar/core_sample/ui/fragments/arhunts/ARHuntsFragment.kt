package com.iar.core_sample.ui.fragments.arhunts


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iar.core_sample.R
import com.iar.core_sample.databinding.ArHuntsFragmentBinding
import com.iar.core_sample.ui.fragments.usermanagement.UserManagementViewModel
import com.iar.iar_core.Hunt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ARHuntsFragment : Fragment() {

    private val viewModel by viewModels<ARHuntsViewModel>()

    private lateinit var binding: ArHuntsFragmentBinding
    private lateinit var huntListView: RecyclerView
    private var huntList: ArrayList<Hunt>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ArHuntsFragmentBinding.inflate(inflater, container, false)
        huntListView = binding.huntList

        viewModel.initialize(requireContext())

        viewModel.userId.observe(viewLifecycleOwner, { userId ->
            userId?.let {
                viewModel.getARHunts()
            }
        })

        viewModel.arHunts.observe(viewLifecycleOwner, { hunts ->
            setupHunts(hunts)
            huntList = hunts
//            for(hunt in hunts){
//                println(hunt.name)
//                println(hunt.id)
//                println(hunt.thumbnailUrl)
//            }

        })



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
               println("hunt item clicked")
            }

        })
        huntListView.adapter = adapter
    }

}