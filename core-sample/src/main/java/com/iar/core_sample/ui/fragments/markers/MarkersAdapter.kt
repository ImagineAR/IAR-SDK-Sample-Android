package com.iar.core_sample.ui.fragments.markers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.iar.common.Utils.loadImage
import com.iar.core_sample.R
import com.iar.iar_core.Marker


class MarkersAdapter(
    private val markersList: List<Marker>,
    private val listener: OnMarkerItemClickListener,
    private val takeMeListener: OnTakeMeThereClickListener
) :
    RecyclerView.Adapter<MarkersAdapter.MarkerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.marker_item, parent, false)
        return MarkerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val marker = markersList[position]

        marker.previewImageUrl?.let {
            holder.markerImage.loadImage(it, holder.markerImage.context)
        }

        holder.markerName.text = marker.name

        var textString = ""
        if (marker.type == "Location") {
            textString = "${marker.id}\n" +
                    "Distance: ${marker.location.distance}"
            holder.nearbyLayout.visibility = View.VISIBLE
            holder.takeMeButton.setOnClickListener {
                takeMeListener.onTakeMeThereClick(marker)
            }
        }

        if (marker.type == "On Demand") {
            textString = marker.id
            holder.nearbyLayout.visibility = View.GONE
        }

        holder.markerId.text = textString

        holder.itemView.setOnClickListener {
            listener.onMarkerItemClick(marker)
        }
    }

    override fun getItemCount(): Int {
        return markersList.size
    }

    class MarkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var markerName: TextView = itemView.findViewById(R.id.marker_name)
        val markerId: TextView = itemView.findViewById(R.id.marker_id)
        val markerImage: ImageView = itemView.findViewById(R.id.marker_image)
        val nearbyLayout: RelativeLayout = itemView.findViewById(R.id.nearby)
        val takeMeButton: MaterialButton = itemView.findViewById(R.id.take_me_button)
    }

    interface OnMarkerItemClickListener {
        fun onMarkerItemClick(marker: Marker)
    }

    interface OnTakeMeThereClickListener {
        fun onTakeMeThereClick(marker: Marker)
    }

}