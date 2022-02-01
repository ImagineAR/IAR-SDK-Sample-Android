package com.iar.surface_ar_sample.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (intent.action != null) {
            nfcViewModel.setIntent(intent)

        }

    }

    override fun onResume() {
        super.onResume()

        nfcController.onResume()
    }

    override fun onPause() {
        super.onPause()
        nfcController.onPause()
    }


}