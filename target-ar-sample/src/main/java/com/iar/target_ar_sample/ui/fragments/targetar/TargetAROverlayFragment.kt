package com.iar.target_ar_sample.ui.fragments.targetar

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.iar.target_ar_sample.databinding.FragmentTargetArOverlayBinding
import com.iar.target_sdk.IARActivity
import kotlinx.coroutines.*

class TargetAROverlayFragment: Fragment() {
    var binding: FragmentTargetArOverlayBinding? = null

    var recordingJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTargetArOverlayBinding.inflate(inflater, container, false)

        context?.let {
            Toast.makeText(it, "Point your camera at an AR target to begin", Toast.LENGTH_LONG).show()
        }

        binding?.screenshotButton?.setOnClickListener {
            (activity as? IARActivity)?.takeScreenshot()
        }

        binding?.videoButton?.setOnClickListener {
            takeVideo()
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

    fun onTrackingChanged(isTracking: Boolean) {
        Handler(Looper.getMainLooper()).post {
            binding?.mediaButtonContainer?.visibility = if (isTracking) View.VISIBLE else View.GONE
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
        (activity as? IARActivity)?.startRecording()

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
        val uri = (activity as? IARActivity)?.stopRecording()
        context?.let {
            Toast.makeText(it, "Video saved at: $uri", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProgress(progress:Int) {
        binding?.progressBar?.progress = progress
    }
}