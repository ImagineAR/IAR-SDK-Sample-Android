package com.iar.target_ar_sample.ui.activities

import android.net.Uri
import android.widget.RelativeLayout
import androidx.activity.viewModels
import com.iar.iar_core.Region
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

    /**
     * This callback gets called when a screenshot is taken. The
     * uri provided will be the path where it is currently saved.
     */
    override fun shareScreenShot(uri: Uri?) {
        // Default behavior brings up a share modal.
        super.shareScreenShot(uri)
    }
}