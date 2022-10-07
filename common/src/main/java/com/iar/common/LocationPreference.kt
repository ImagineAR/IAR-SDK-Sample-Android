package com.iar.common

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
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
            editLocation.setText(
                sharedPreferences?.getString(
                    context.getString(R.string.location_coordinates_key),
                    ""
                ) ?: ""
            )
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
                if (button.text == context.getString(R.string.center_of_na)) {
                    editLocation.setText(context.getString(R.string.center_coordinates))
                }

                val text = editLocation.text.toString()

                button.text = context.getString(R.string.center_of_na)

                if (text.isNotEmpty() && checkValidCoordinates(text)) {
                    with(sharedPreferences?.edit()) {
                        this?.putString(context.getString(R.string.location_coordinates_key), text)
                        this?.apply()
                    }

                } else {
                    button.text = context.getString(R.string.apply)
                    Utils.showToastMessage(context.getString(R.string.enter_valid_coordinates), context)
                }

                //Hide keyboard
                val imm = ContextCompat.getSystemService(context, InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(it.windowToken, 0)
            }

        } else {
            editLocation.setText("")
            button.isEnabled = false
            editLocation.isFocusable = false
            editLocation.isFocusableInTouchMode = false
        }
    }

    //Add this function here temporarily, should add this in SDK
    private fun checkValidCoordinates(coordinates: String): Boolean{
        val positionString = coordinates.split("[\\s,]+".toRegex()).toTypedArray()

        if (positionString.size > 1) {
            try {
                positionString[0].toDouble()
                positionString[1].toDouble()
                return true
            } catch (e: NumberFormatException) {
                return false
            }
        }
        return false
    }
}