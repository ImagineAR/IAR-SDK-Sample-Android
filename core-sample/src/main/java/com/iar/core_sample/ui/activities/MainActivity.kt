package com.iar.core_sample.ui.activities

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.iar.core_sample.databinding.ActivityMainBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
}