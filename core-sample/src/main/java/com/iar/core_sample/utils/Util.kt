package com.iar.core_sample.utils

import android.content.Context
import android.text.InputType
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import com.iar.common.Utils.dpToPx

object Util {

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
}