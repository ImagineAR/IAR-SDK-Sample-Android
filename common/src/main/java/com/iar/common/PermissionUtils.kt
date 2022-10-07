package com.iar.common

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import androidx.annotation.RequiresApi
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import java.util.ArrayList

object PermissionUtils {
    private const val PERMISSION_REQUEST_CODE = 1000

    /**
     * Checks for app permission
     * @param activity
     * The parent activity
     * @param permissionTypes
     * The permission being asked, i.e. Manifest.permission.CAMERA
     * @return
     */
    fun checkPermission(activity: Activity, permissionTypes: Array<String>): Boolean {
        val remainingPermissions: MutableList<String> = ArrayList()
        val deniedPermissions: MutableList<String> = ArrayList()

        for (permissionType in permissionTypes) {
            if (ContextCompat.checkSelfPermission(activity, permissionType) != PackageManager.PERMISSION_GRANTED) {
                if (neverAskAgainSelected(activity, permissionType)) {
                    deniedPermissions.add(permissionType)
                } else {
                    remainingPermissions.add(permissionType)
                }
            }
        }
        return when {
            deniedPermissions.isNotEmpty() -> {
                displayNeverAskAgainDialog(activity, deniedPermissions.toTypedArray())
                false
            }
            remainingPermissions.isNotEmpty() -> {
                activity.requestPermissions(permissionTypes, PERMISSION_REQUEST_CODE)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun neverAskAgainSelected(activity: Activity, permission: String): Boolean {
        val prevShouldShowStatus = getRationaleDisplayStatus(activity, permission)
        val currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission)
        return prevShouldShowStatus != currShouldShowStatus
    }

    private fun getRationaleDisplayStatus(context: Context, permission: String): Boolean {
        val genPrefs = context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE)
        return genPrefs.getBoolean(permission, false)
    }

    private fun displayNeverAskAgainDialog(activity: Activity, deniedPermissions: Array<String>) {
        val builder = AlertDialog.Builder(activity, R.style.AppDialog)
        var message = activity.getString(R.string.denied_permission_message)

        // Check which permissions were denied.
        var cameraDenied = false
        var storageDenied = false
        for (p in deniedPermissions) {
            if (p.contains(Manifest.permission.CAMERA)) cameraDenied = true
            if (p.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) storageDenied = true
            if (p.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) storageDenied = true
        }
        if (cameraDenied) {
            message += activity.getString(R.string.camera)
        }
        if (storageDenied) {
            message += activity.getString(R.string.storage_access)
        }
        message += activity.getString(R.string.permission_instruction)
        builder.setTitle(activity.getString(R.string.oops))
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(activity.getString(R.string.permit_manually)) { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivity(intent)
        }
        builder.setNegativeButton(activity.getString(R.string.cancel), null)
        builder.show()
    }
}