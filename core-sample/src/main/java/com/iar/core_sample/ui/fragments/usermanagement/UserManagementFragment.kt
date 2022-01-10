package com.iar.core_sample.ui.fragments.usermanagement

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.iar.core_sample.R
import com.iar.core_sample.databinding.UserManagementFragmentBinding
import com.iar.core_sample.ui.common.BaseFragment
import com.iar.core_sample.ui.common.BaseViewModel
import com.iar.core_sample.utils.Util.setupDialogEditText
import com.iar.iar_core.CoreAPI
import com.iar.iar_core.Region
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UserManagementFragment : BaseFragment() {
    private val LOGTAG = "userManagementFragment"
    private val viewModel by viewModels<UserManagementViewModel>()
    private val args by navArgs<UserManagementFragmentArgs>()

    override fun getViewModel(): BaseViewModel = viewModel

    private lateinit var binding: UserManagementFragmentBinding
    private var isAnonymous = true
    private var hasUserLoaded = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val orgKey = args.orgKey
        val region = args.region
     //   val orgKey = arguments?.getString("OrgKey")?:""
    //    val region = arguments?.getString("Region")?:""


        Log.d(LOGTAG, orgKey)
        Log.d(LOGTAG, region)


        viewModel.initialize(orgKey, Region.valueOf(region), requireContext())


        binding = UserManagementFragmentBinding.inflate(inflater, container, false)

        viewModel.loadCurrentUser(requireContext(), {
            hasUserLoaded = true

        }) { errCode, errMsg ->
            hasUserLoaded = false

        }
        isAnonymous = viewModel.getIsAnonymous(requireActivity());

        if (!isAnonymous) {
            binding.createButton.setVisibility(View.GONE);
            binding.logButton.setText(R.string.logout);
        } else {
            binding.createButton.setVisibility(View.VISIBLE);
            binding.logButton.setText(getString(R.string.login));
        }
        val userId = viewModel.getCurrentUserId()

        binding.userId.setText(userId)


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

        if (oldUserId != null) {
            viewModel.migrateUser(requireActivity(),
                oldUserId,
                newUserId,
                {
                    binding.userId.text = newUserId
                    binding.logButton.setText(getString(R.string.logout))
                    Log.i(LOGTAG, "Migrate successfully: $newUserId")
                }
            ) { errorCode, errorMessage ->
                Log.i(LOGTAG, "Fail to migrate: $errorCode $errorMessage")

            }
        } else {
            val toast = Toast.makeText(
                requireActivity().getApplicationContext(),
                "Did not have user Id yet",
                Toast.LENGTH_SHORT
            )
            toast.show()
        }
    }

    private fun login(inputId: String) {
        viewModel.login(requireContext(),
            inputId,
            {
                binding.userId.text = inputId
                binding.createButton.setVisibility(View.GONE)
                binding.logButton.setText(getString(R.string.logout))
                Log.i(LOGTAG, "Login successfully")

            }
        ) { errorCode, errorMessage ->
            Log.i(LOGTAG, "Login: $errorMessage")
            binding.userId.setText("")
            val toast =
                Toast.makeText(
                    requireActivity().getApplicationContext(),
                    "Login failed",
                    Toast.LENGTH_SHORT
                )
            toast.show()

        }
    }

    private fun createNewUser(inputId: String) {
        viewModel.createNewUser(requireContext(),
            inputId,
            {
                binding.userId.setText(inputId)
                binding.createButton.setVisibility(View.GONE)
                binding.logButton.setText(getString(R.string.logout))
                Log.i(LOGTAG, "Create new user successfully")

            }
        ) { errorCode, errorMessage ->
            Log.i(LOGTAG, "Create New User: $errorMessage")

            val toast = Toast.makeText(
                requireActivity().getApplicationContext(),
                "Failed to create User Id, This user id has existed! ",
                Toast.LENGTH_SHORT
            )
            toast.show()

        }
    }

    private fun setExternalUser() {
        val buttonText: String = binding.logButton.text.toString()

        if (buttonText == "Login") {
            userDialog(getString(R.string.login_user), false, false)
        }

        if (buttonText == "Logout") {
            viewModel.logout(requireActivity(), {
                binding.createButton.setVisibility(View.VISIBLE)
                binding.logButton.setText(getString(R.string.login))
                val userId = viewModel.getCurrentUserId()
                binding.userId.text = userId
                Log.d(LOGTAG, "Logout successfully")

            }
            ) { errorCode, errorMessage ->
                Log.d(LOGTAG, "Logout: $errorCode $errorMessage")

            }
        }
    }

    private fun userDialog(title: String, createNew: Boolean, isMigrate: Boolean) {
        val builder: android.app.AlertDialog.Builder =
            android.app.AlertDialog.Builder(requireActivity())
        builder.setTitle(title)
        val oldUserId: String = viewModel.getCurrentUserId()

        val container = FrameLayout(requireActivity())
        val editText: EditText = setupDialogEditText(requireContext())
        container.addView(editText)
        builder.setView(container)

        if (isMigrate) {
            builder.setMessage(getString(R.string.migrate_message))
            editText.setText(oldUserId)
            editText.textAlignment= View.TEXT_ALIGNMENT_CENTER
        } else {
            builder.setMessage(getString(R.string.enter_external_unserId))
        }

        builder.setPositiveButton(getString(R.string.ok)) { dialogInterface, i ->
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