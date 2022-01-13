package com.iar.core_sample.utils

import android.content.Context
import android.text.InputType
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import com.google.gson.Gson
import com.iar.core_sample.R

object Util {

    val gson = Gson()

    fun setupDialogEditText(context: Context): EditText {
        val padding =dpToPx(20, context).toInt()
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
        val r= context.getResources()
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        )
    }


}