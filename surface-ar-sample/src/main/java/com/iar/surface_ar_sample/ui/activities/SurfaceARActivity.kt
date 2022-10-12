package com.iar.surface_ar_sample.ui.activities

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import com.iar.common.Utils
import com.iar.surface_ar_sample.R
import com.iar.surface_ar_sample.ui.fragments.surfacear.SurfaceAROverlayFragment
import com.iar.surface_sdk.aractivity.IARSurfaceActivity
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class SurfaceARActivity : IARSurfaceActivity() {
    private val viewModel by viewModels<SurfaceARViewModel>()

    private var mOverlay: WeakReference<SurfaceAROverlayFragment>? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        //provide your own customized UI/behaviour
        if (!this.isARCoreSupported()) {
            println(getString(R.string.device_not_support_arcore))
            Toast.makeText(
                applicationContext,
                getString(R.string.device_not_support_arcore),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Here we can override the function provided by IARActivity
     * to add our custom overlay over the camera view.
     * This can provide better customization to the UI.
     * Typically this overlay can have a targeting
     * reticule or help text to provide more context
     * on what the user should do.
     */
    override fun onSetupOverlayViews(contentView: FrameLayout) {
        val overlayFragment = SurfaceAROverlayFragment()

        // Add our custom overlay fragment to the container view on top of the camera view.
        contentView.let {
            val fm = this.supportFragmentManager
            val ft = fm.beginTransaction()
            ft.add(it.id, overlayFragment).commit()

            // Set a reference to our fragment so we can propagate IARActivity events
            // to trigger UI states.
            mOverlay = WeakReference(overlayFragment)
        }
    }

    /**
     * We can override this to detect when an asset is anchored or not.
     * We just pass this to the overlay which should handle the logic of what to display.
     */
    override fun onAssetAnchored(isPlaced: Boolean) {
        super.onAssetAnchored(isPlaced)
        mOverlay?.get()?.onAssetAnchored(isPlaced)
    }

    override fun onVideoRecordingSaved(fileUri: Uri?, fileName: String?) {
        fileUri?.let { uri ->
            fileName?.let { file ->
                mOverlay?.get()?.onVideoRecordingSaved(uri)
            }
        }
    }

    override fun onScreenshotCapture(fileUri: Uri?, fileName: String?) {
        super.onScreenshotCapture(fileUri, fileName)
        if (fileUri != null) {
            Utils.shareScreenShot(fileUri, this)
        } else {
            Utils.showToastMessage(getString(R.string.no_screenshot_file), this)
        }
    }

    override fun onVideoEnd() {
        super.onVideoEnd()
        playVideo()
    }

    override fun surfaceViewSurfaceDetected() {
        mOverlay?.get()?.onSurfaceDetected()
    }

}