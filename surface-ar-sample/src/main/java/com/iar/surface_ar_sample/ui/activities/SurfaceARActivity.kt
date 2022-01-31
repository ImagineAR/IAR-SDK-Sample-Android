package com.iar.surface_ar_sample.ui.activities

import android.widget.FrameLayout
import androidx.activity.viewModels
import com.iar.surface_ar_sample.ui.fragments.targetar.SurfaceAROverlayFragment
import com.iar.surface_sdk.aractivity.IARSurfaceActivity
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class SurfaceARActivity: IARSurfaceActivity() {
    private val viewModel by viewModels<SurfaceARViewModel>()

    private var mOverlay: WeakReference<SurfaceAROverlayFragment>? = null

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
}