package com.iar.core_sample.utils

import android.content.Context
import android.text.InputType
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.gson.Gson
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.iar.core_sample.R

object Util {

    val gson = Gson()

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
        editText.setBackgroundResource(R.drawable.edit_text_border)
        editText.inputType = InputType.TYPE_CLASS_TEXT
        editText.isSingleLine = false
        editText.maxLines = 2
        editText.textSize = 14f

        return editText
    }

    fun dpToPx(dp: Int, context: Context): Float {
        val r = context.getResources()
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        )
    }

    fun showToastMessage(message: String, context: Context) {
        val toast = Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        )
        toast.show()
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

}