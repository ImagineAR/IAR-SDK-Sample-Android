package com.iar.surface_ar_sample.ui.activities


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.iar.common.PermissionUtils
import com.iar.common.Utils
import com.iar.nfc_sdk.NFCController
import com.iar.surface_ar_sample.databinding.ActivityMainBinding
import com.iar.surface_ar_sample.ui.fragments.nfc.NFCViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val nfcViewModel by viewModels<NFCViewModel>()

    private lateinit var nfcController: NFCController

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nfcController = NFCController(this)
        nfcViewModel.setNfcController(nfcController)

        PermissionUtils.checkPermission(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            )
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var hasDeniedAPermission = false
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                hasDeniedAPermission = true
            }
        }

        if (hasDeniedAPermission) {
            Toast.makeText(
                applicationContext,
                "All permissions requested are necessary for the app to run properly.",
                Toast.LENGTH_LONG
            ).show()

            finish()
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intent.action != null) {
            nfcViewModel.setIntent(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (Utils.checkNFCSupported(this)) {
            nfcController.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Utils.checkNFCSupported(this)) {
            nfcController.onPause()
        }
    }

}