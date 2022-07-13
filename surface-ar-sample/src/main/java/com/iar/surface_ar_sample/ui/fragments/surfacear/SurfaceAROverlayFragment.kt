package com.iar.surface_ar_sample.ui.fragments.surfacear

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.iar.common.Constants
import com.iar.surface_ar_sample.R
import com.iar.surface_ar_sample.databinding.FragmentSurfaceArOverlayBinding
import com.iar.common.PreviewVideoActivity
import com.iar.surface_sdk.aractivity.IARSurfaceActivity
import kotlinx.coroutines.*

class SurfaceAROverlayFragment: Fragment() {
    var binding: FragmentSurfaceArOverlayBinding? = null

    var recordingJob: Job? = null
    var isAnchored = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSurfaceArOverlayBinding.inflate(inflater, container, false)

        context?.let {
            Toast.makeText(it, "Point your camera at a surface to place", Toast.LENGTH_LONG).show()
        }

        binding?.screenshotButton?.setOnClickListener {
            (activity as? IARSurfaceActivity)?.takeScreenshot()
        }

        binding?.videoButton?.setOnClickListener {
            takeVideo()
        }

        binding?.placeMoveButton?.setOnClickListener {
            if (isAnchored) (activity as? IARSurfaceActivity)?.unanchorAsset()
            else (activity as? IARSurfaceActivity)?.anchorAsset()
        }

        return binding?.root
    }

    override fun onPause() {
        super.onPause()

        // Cancel recording if it has started.
        recordingJob?.let {
            it.cancel()
            stopRecording()
            recordingJob = null
        }
    }

    fun onAssetAnchored(isPlaced: Boolean) {
        isAnchored = isPlaced
        Handler(Looper.getMainLooper()).post {
            val visibility = if (isPlaced) View.VISIBLE else View.GONE
            binding?.screenshotButton?.visibility = visibility
            binding?.videoButton?.visibility = visibility
            binding?.placeMoveButton?.text =
                if(isPlaced) getString(R.string.button_label_move)
                else getString(R.string.button_label_place)
        }
    }

    private fun takeVideo() {
        // Start taking video, then display progress of video. We will
        // only take a 10 second video in this sample app.

        // Disable the button so we don't record multiple videos at the same time...
        binding?.videoButton?.isEnabled = false

        // Show the progress bar of recording.
        binding?.progressBar?.visibility = View.VISIBLE

        // Start our recording.
        (activity as? IARSurfaceActivity)?.startRecording()

        // Get a handle to the job so that it is cancellable whenever we want
        // by calling job.cancel()
        // Here we use lifecyclescope since we want this job to only exist in a valid
        // lifecycle state.
        recordingJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            var currentProgress = 0
            val maxTime = 5000
            val interval = 500L

            while(currentProgress < maxTime) {
                delay(interval)
                currentProgress += interval.toInt()

                withContext(Dispatchers.Main) {
                    updateProgress(currentProgress)
                }
            }

            withContext(Dispatchers.Main) {
                stopRecording()
                recordingJob = null
            }
        }
    }

    private fun stopRecording() {
        binding?.progressBar?.visibility = View.GONE
        binding?.videoButton?.isEnabled = true
        (activity as? IARSurfaceActivity)?.stopRecording()
    }

    private fun updateProgress(progress:Int) {
        binding?.progressBar?.progress = progress
    }

    fun onVideoRecordingSaved(fileUri: Uri) {
        Log.d("SurfaceAROverlayFragment", "Video saved at: $fileUri")
        goToPreviewVideo(fileUri)

    }

    private fun goToPreviewVideo(uri: Uri) {
        val intent = Intent(activity, PreviewVideoActivity::class.java).apply {
            putExtra(Constants.EXTRAS_VIDEO_URI, uri.toString())
        }
        startActivity(intent)
    }
}