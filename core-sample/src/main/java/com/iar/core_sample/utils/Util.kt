package com.iar.core_sample.utils

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.InputType
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
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

    fun RecyclerView.addDivider(context: Context){
        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(ColorDrawable(ContextCompat.getColor(context, R.color.lightGrey)));
        this.addItemDecoration(dividerItemDecoration)
    }

}