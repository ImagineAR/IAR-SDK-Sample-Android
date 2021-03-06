package com.iar.surface_ar_sample.ui.fragments.ondemand

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iar.common.Utils.loadImage
import com.iar.iar_core.Marker
import com.iar.surface_ar_sample.R


class MarkersAdapter(
    private val markersList: List<Marker>,
    val listener: OnMarkerItemClickListener
) :
    RecyclerView.Adapter<MarkersAdapter.MarkerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.marker_item, parent, false)
        return MarkerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val marker = markersList[position]

        holder.markerImage.context?.let { imageContext ->
            marker.previewImageUrl?.let {
                holder.markerImage.loadImage(it, imageContext)
            }
        }


        holder.markerName.text = marker.name
        holder.markerId.text = marker.id
        holder.itemView.setOnClickListener {
            listener.onMarkerItemClick(marker)
        }
    }

    override fun getItemCount(): Int {
        return markersList.size
    }

    class MarkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var markerName = itemView.findViewById<TextView>(R.id.marker_name)
        val markerId = itemView.findViewById<TextView>(R.id.marker_id)
        val markerImage = itemView.findViewById<ImageView>(R.id.marker_image)
    }

    interface OnMarkerItemClickListener {
        fun onMarkerItemClick(marker: Marker)
    }

}