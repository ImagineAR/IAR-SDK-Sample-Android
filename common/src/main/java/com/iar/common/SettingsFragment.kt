package com.iar.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.iar.iar_core.debugshell.DevConsoleDialog
import com.iar.iar_core.debugshell.DevConsoleDialog.DevConsoleListener


class SettingsFragment(private var applicationId: String) : PreferenceFragmentCompat(), DevConsoleListener {
    private var devConsoleDialog: DevConsoleDialog? = null
    private var rootKey: String? = null

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater!!, container, savedInstanceState)
        context?.getColor(android.R.color.background_dark)?.let {
            view.setBackgroundColor(it)
        }
        return view
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        this.rootKey = rootKey

        setupPreferences()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val swipeView: View? = view.findViewById(R.id.swipeView)

        swipeView?.setOnTouchListener(object : OnSwipeTouchListener(activity) {

            override fun onSwipeRight() {
                activity?.supportFragmentManager?.popBackStack()
            }
        })
    }

    override fun onDismiss() {
        devConsoleDialog = null
        setupPreferences()
    }

    private fun setupPreferences() {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val debugMode =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.debug_mode_key))
        val logSwitch =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.log_key))
        val apiLogSwitch =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.api_log_key))
        val analyticsSwitch =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.analytics_key))
        val analyticsLogSwitch =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.analytics_log_key))
        val validateAnalyticsLogSwitch =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.validate_analytics_log_key))
        val arDebugSwitch =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.ar_debug_key))
        val arPointsSwitch =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.ar_points_key))
        val arShadowsSwitch =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.ar_shadows_key))
        val lightDetectionSwitch =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.light_detection_key))
        val simulatedLocationSwitch =
            preferenceManager.findPreference<SwitchPreferenceCompat>(getString(R.string.simulated_location_key))

        val locationCoordinates =
            preferenceManager.findPreference<Preference>(getString(R.string.location_coordinates_key))
        locationCoordinates?.isEnabled = simulatedLocationSwitch?.isChecked ?: false

        preferenceManager.findPreference<DebugButtonPreference>("debugButton")?.clickListener = listener@{
            if(devConsoleDialog != null) return@listener
            devConsoleDialog = DevConsoleDialog.show(parentFragmentManager,
                null,
                "$applicationId.provider",
                this@SettingsFragment)
        }

        debugMode?.setOnPreferenceChangeListener { _, _ ->
            logSwitch?.isChecked = false
            apiLogSwitch?.isChecked = false
            analyticsSwitch?.isChecked = false
            analyticsLogSwitch?.isChecked = false
            validateAnalyticsLogSwitch?.isChecked = false
            arDebugSwitch?.isChecked = false
            arPointsSwitch?.isChecked = false
            arShadowsSwitch?.isChecked = true
            lightDetectionSwitch?.isChecked = false
            simulatedLocationSwitch?.isChecked = false
            true
        }

        logSwitch?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == false) {
                apiLogSwitch?.isChecked = false
                analyticsLogSwitch?.isChecked = false
                validateAnalyticsLogSwitch?.isChecked = false
            }

            true
        }

        apiLogSwitch?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == true) {
                logSwitch?.isChecked = true
            }

            true
        }

        if (analyticsSwitch?.isChecked == false) {
            analyticsLogSwitch?.isChecked = false
            validateAnalyticsLogSwitch?.isChecked = false
            analyticsLogSwitch?.isEnabled = false
            validateAnalyticsLogSwitch?.isEnabled = false
        }

        analyticsSwitch?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == false) {
                analyticsLogSwitch?.isChecked = false
                validateAnalyticsLogSwitch?.isChecked = false
                analyticsLogSwitch?.isEnabled = false
                validateAnalyticsLogSwitch?.isEnabled = false

            } else {
                analyticsLogSwitch?.isEnabled = true
                validateAnalyticsLogSwitch?.isEnabled = true
            }

            true
        }

        analyticsLogSwitch?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == true) {
                logSwitch?.isChecked = true
            }
            true
        }

        validateAnalyticsLogSwitch?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == true) {
                logSwitch?.isChecked = true
            }
            true
        }

        arDebugSwitch?.setOnPreferenceChangeListener { _, newValue ->
            true
        }

        arPointsSwitch?.setOnPreferenceChangeListener { _, newValue ->
            true
        }

        arShadowsSwitch?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == false) {
                lightDetectionSwitch?.isChecked = false
            }

            true
        }

        lightDetectionSwitch?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == true) {
                arShadowsSwitch?.isChecked = true
            }
            true
        }

        simulatedLocationSwitch?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == true) {
                locationCoordinates?.isEnabled = true
            }

            if (newValue == false) {
                locationCoordinates?.isEnabled = false
            }

            true
        }
    }

}