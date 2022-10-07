package com.iar.common.user

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.iar.common.R
import com.iar.common.Utils.setupDialogEditText
import com.iar.common.Utils.showToastMessage
import com.iar.common.base.BaseFragment
import com.iar.common.base.BaseViewModel
import com.iar.common.databinding.UserFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : BaseFragment() {
    private val viewModel by viewModels<UserViewModel>()
    override fun getViewModel(): BaseViewModel = viewModel

    private lateinit var binding:UserFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UserFragmentBinding.inflate(inflater,container, false)
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
                showToastMessage("${getString(R.string.there_is_error)} $error", requireContext())
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

    private fun migrateUser(oldUserId: String?, isMigrate: Boolean) {
        oldUserId?.let {
            viewModel.migrateUser(
                requireActivity(),
                oldUserId,
                isMigrate
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

        if (buttonText == getString(R.string.login)) {
            userDialog(getString(R.string.login_user), false, false)
        }

        if (buttonText == getString(R.string.logout)) {
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

            builder.setNeutralButton(R.string.migrate) { dialogInterface, i ->
                migrateUser(oldUserId,true)
            }
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
                migrateUser(oldUserId,false)
            }

            dialogInterface.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        builder.create().show()
    }

}