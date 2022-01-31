package com.iar.core_sample.ui.fragments.usermanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.iar.common.Utils.showToastMessage
import com.iar.core_sample.R
import com.iar.core_sample.databinding.UserManagementFragmentBinding
import com.iar.core_sample.ui.common.BaseFragment
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.core_sample.utils.Util.setupDialogEditText
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UserManagementFragment : BaseFragment() {

    private val viewModel by viewModels<UserManagementViewModel>()

    override fun getViewModel(): BaseViewModel = viewModel

    private lateinit var binding: UserManagementFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.initialize(requireContext())

        binding = UserManagementFragmentBinding.inflate(inflater, container, false)

        viewModel.loadCurrentUser(requireContext())

        viewModel.isAnonymous.observe(viewLifecycleOwner) { isAnonymous ->
            if (!isAnonymous) {
                binding.createButton.visibility = View.GONE;
            } else {
                binding.createButton.visibility = View.VISIBLE;
            }
        }

        viewModel.userId.observe(viewLifecycleOwner) { userId ->
            binding.userId.text = userId
        }

        viewModel.isLogin.observe(viewLifecycleOwner) { isLogin ->
            if (isLogin) {
                binding.logButton.setText(R.string.logout)
            } else {
                binding.logButton.setText(R.string.login)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showToastMessage("There is error $error", requireContext())
            }
        }

        binding.createButton.setOnClickListener {
            userDialog(getString(R.string.create_new_user), true, false)
        }

        binding.logButton.setOnClickListener {
            setExternalUser()
        }

        binding.migrateButton.setOnClickListener {
            userDialog(getString(R.string.migrate_user), false, true)
        }

        return binding.root
    }

    private fun migrateUser(oldUserId: String?) {
        val newUserId = UUID.randomUUID().toString()

        oldUserId?.let {
            viewModel.migrateUser(
                requireActivity(),
                oldUserId,
                newUserId
            )

        }
    }

    private fun login(inputId: String) {
        viewModel.login(requireContext(), inputId)
        binding.userId.text = inputId
    }

    private fun createNewUser(inputId: String) {
        viewModel.createNewUser(
            requireContext(),
            inputId
        )

    }

    private fun setExternalUser() {
        val buttonText: String = binding.logButton.text.toString()

        if (buttonText == "Login") {
            userDialog(getString(R.string.login_user), false, false)
        }

        if (buttonText == "Logout") {
            viewModel.logout(requireActivity())
        }
    }

    private fun userDialog(title: String, createNew: Boolean, isMigrate: Boolean) {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle(title)

        var oldUserId = ""

        viewModel.userId.observe(viewLifecycleOwner) {
            oldUserId = it
        }

        val container = FrameLayout(requireActivity())
        val editText: EditText = setupDialogEditText(requireContext())
        container.addView(editText)
        builder.setView(container)

        if (isMigrate) {
            builder.setMessage(getString(R.string.migrate_message))
            editText.setText(oldUserId)
            editText.textAlignment = View.TEXT_ALIGNMENT_CENTER
        } else {
            builder.setMessage(getString(R.string.enter_external_unserId))
        }

        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
            val inputId = editText.text.toString()
            if (!isMigrate) {
                if (!createNew) {
                    login(inputId)

                } else {
                    createNewUser(inputId)
                }
            } else {
                migrateUser(oldUserId)
            }

            dialogInterface.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.create().show()
    }

}