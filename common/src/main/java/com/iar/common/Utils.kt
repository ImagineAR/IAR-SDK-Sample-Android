package com.iar.common

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.widget.ImageView
import android.widget.Toast
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
}