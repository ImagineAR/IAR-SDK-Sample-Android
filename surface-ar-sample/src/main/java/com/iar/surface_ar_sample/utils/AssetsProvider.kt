package com.iar.surface_ar_sample.utils

import android.content.ContentProvider
import kotlin.Throws
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import java.io.FileNotFoundException
import java.io.IOException

class AssetsProvider : ContentProvider() {
    @Throws(FileNotFoundException::class)
    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        val am = context!!.assets
        val file_name = uri.lastPathSegment ?: throw FileNotFoundException()
        var afd: AssetFileDescriptor? = null
        try {
            afd = am.openFd(file_name)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return afd //super.openAssetFile(uri, mode);
    }

    override fun onCreate(): Boolean {
        return false
    }

    override fun query(
        uri: Uri,
        strings: Array<String>?,
        s: String?,
        strings1: Array<String>?,
        s1: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String>?
    ): Int {
        return 0
    }
}