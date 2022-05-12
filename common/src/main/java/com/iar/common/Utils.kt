package com.iar.common

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.InputType
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson

object Utils {
    val gson = Gson()

    fun RecyclerView.addDivider(context: Context, colorResId: Int){
        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(ColorDrawable(ContextCompat.getColor(context, colorResId)));
        this.addItemDecoration(dividerItemDecoration)
    }

    fun showToastMessage(message: String, context: Context) {
        val toast = Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        )
        toast.show()
    }

    fun dpToPx(dp: Int, context: Context): Float {
        val r = context.getResources()
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        )
    }

    fun ImageView.loadImage(url: String, context: Context) {
        val requestOptions = RequestOptions()
            .override(400, 400)
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.splash_icon)
            .error(R.drawable.splash_icon)
            .apply(requestOptions)
            .into(this)
    }

    fun setupDialogEditText(context: Context): EditText {
        val padding = dpToPx(20, context).toInt()
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.leftMargin = padding
        params.rightMargin = padding
        val editText = EditText(context)
        editText.layoutParams = params
        editText.inputType = InputType.TYPE_CLASS_TEXT
        editText.isSingleLine = false
        editText.maxLines = 2
        editText.textSize = 14f
        return editText
    }

    fun checkNFCSupported(context: Context): Boolean {
        val pm = context.packageManager
        return pm.hasSystemFeature(PackageManager.FEATURE_NFC)
    }

    fun shareScreenShot(uri: Uri, activity: AppCompatActivity) {
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.setType("image/*")
        sendIntent.putExtra(Intent.EXTRA_STREAM, uri)
        val shareIntent: Intent = Intent.createChooser(sendIntent, null)
        activity.startActivity(shareIntent)
    }
}