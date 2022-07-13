package com.iar.common

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.MediaController
import android.widget.SeekBar
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.iar.common.databinding.ActivityPreviewVideoBinding


class PreviewVideoActivity : AppCompatActivity() {

    private lateinit var previewVideoView: VideoView
    private lateinit var seekBar: SeekBar
    private lateinit var mediaControls: MediaController
    private val updateHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPreviewVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uri = intent.getStringExtra(Constants.EXTRAS_VIDEO_URI)
        mediaControls = MediaController(this)
        previewVideoView = binding.previewVideo
        seekBar = binding.seekBar

        mediaControls.setAnchorView(previewVideoView)

        previewVideoView.apply {
            setMediaController(mediaControls)
            setVideoURI(Uri.parse(uri))
            requestFocus()
            start()
        }

        previewVideoView.setOnPreparedListener { mediaPlayer ->
            val duration = previewVideoView.duration
            binding.seekBar.progress = 0
            binding.seekBar.max = duration
            updateHandler.postDelayed(updateVideoTime, 100)
        }

        binding.ShareButton.setOnClickListener {
            Utils.shareScreenShot(Uri.parse(uri), this)
        }
    }

    private val updateVideoTime: Runnable = object : Runnable {
        override fun run() {
            val currentPosition: Int = previewVideoView.currentPosition
            seekBar.progress = currentPosition
            updateHandler.postDelayed(this, 100)
        }
    }
}