package com.iar.target_ar_sample.ui.activities

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.RelativeLayout
import androidx.activity.viewModels
import com.iar.common.Utils
import com.iar.iar_core.Constants.UPDATE_ERROR_NO_NETWORK_CONNECTION

import com.iar.iar_core.IARError
import com.iar.iar_core.Region
import com.iar.target_ar_sample.R
import com.iar.target_ar_sample.ui.fragments.targetar.TargetAROverlayFragment
import com.iar.target_sdk.IARActivity
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class TargetARActivity: IARActivity() {
    private val viewModel by viewModels<TargetARViewModel>()

    private var mOverlay: WeakReference<TargetAROverlayFragment>? = null

    override fun setServerRegion(): Region = viewModel.getRegion()

    override fun setIARLicense(): String = viewModel.getLicense()

    /**
     * Here we can override the function provided by IARActivity
     * to add our custom overlay over the camera view.
     * This can provide better customization to the UI.
     * Typically this overlay can have a targeting
     * reticule or help text to provide more context
     * on what the user should do.
     */
    override fun onSetupOverlayViews(contentView: RelativeLayout?) {

        val overlayFragment = TargetAROverlayFragment()

        // Add our custom overlay fragment to the container view on top of the camera view.
        contentView?.let {
            val fm = this.supportFragmentManager
            val ft = fm.beginTransaction()
            ft.add(it.id, overlayFragment).commit()

            // Set a reference to our fragment so we can propagate IARActivity events
            // to trigger UI states.
            mOverlay = WeakReference(overlayFragment)
        }
    }

    /**
     * Here we can override this method from IARActivity to trigger
     * displaying the screenshot/record button on our overlay fragment.
     * We just propagate this event to the overlay fragment and let the
     * overlay fragment handle the rest.
     */
    override fun onTrackingChanged(isTracking: Boolean) {
        mOverlay?.get()?.onTrackingChanged(isTracking)
    }

    override fun onVideoRecordingSaved(uri: Uri?) {
        super.onVideoRecordingSaved(uri)

        if (uri != null) {
            mOverlay?.get()?.onVideoRecordingSaved(uri)
        } else {
            Utils.showToastMessage(getString(R.string.no_video_file), this)
        }
    }

    override fun onScreenshotCapture(uri: Uri?) {
        super.onScreenshotCapture(uri)
        if (uri != null) {
            Utils.shareScreenShot(uri, this)
        } else {
            Utils.showToastMessage(getString(R.string.no_screenshot_file), this)
        }
    }

    override fun onError(errorCode: IARError?, errorDesc: String?, errorTime: Double): Boolean {

        errorDesc?.let{
            if(it.contains("No network connection detected.")){
                Handler(Looper.getMainLooper()).postDelayed({
                    Utils.showToastMessage(getString(R.string.no_internet_connection), this)
                    finish()
                },1000)

            }
        }

        return super.onError(errorCode, errorDesc, errorTime)

    }

}