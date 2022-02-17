package com.iar.common

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.EditText
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder

class LocationPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : Preference(context, attrs, defStyleAttr) {

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.isClickable = false
        val editLocation = holder.findViewById(R.id.edit_location) as EditText
        val button = holder.findViewById(R.id.center_button) as Button

        if (isEnabled) {
            editLocation.setText(sharedPreferences?.getString(context.getString(R.string.location_coordinates_key), "") ?: "")
            button.isEnabled = true
            editLocation.isFocusable = true
            editLocation.isFocusableInTouchMode = true

            editLocation.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    button.text = context.getString(R.string.apply)
                    editLocation.setText("")
                }
            }

            button.isClickable = true
            button.setOnClickListener {

                if (button.text == "Center of NA") {
                    editLocation.setText(context.getString(R.string.center_coordinates))
                }

                val text = editLocation.text.toString()

                button.text = context.getString(R.string.center_of_na)

                with(sharedPreferences?.edit()) {
                    this?.putString(context.getString(R.string.location_coordinates_key), text)
                    this?.apply()
                }
            }

        } else {
            editLocation.setText("")
            button.isEnabled = false
            editLocation.isFocusable = false
            editLocation.isFocusableInTouchMode = false
        }
    }
}