package com.iar.surface_ar_sample.ui.fragments.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iar.common.Utils.loadImage
import com.iar.iar_core.Marker
import com.iar.surface_ar_sample.R


class LocationMakersAdapter (
    private val markersList: List<Marker>,
    val listener: OnLocationMarkerItemClickListener
): RecyclerView.Adapter<LocationMakersAdapter.MarkerViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.location_marker_item, parent, false)
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
        val distanceString = "Distance: ${marker.location.distance}"
        holder.markerDistance.text = distanceString
        holder.itemView.setOnClickListener {
            listener.onMarkerItemClick(marker)
        }
    }

    override fun getItemCount(): Int {
        return markersList.size
    }

    class MarkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var markerName: TextView = itemView.findViewById<TextView>(R.id.marker_name)
        val markerId: TextView = itemView.findViewById<TextView>(R.id.marker_id)
        val markerImage: ImageView = itemView.findViewById<ImageView>(R.id.marker_image)
        val markerDistance: TextView = itemView.findViewById<TextView>(R.id.marker_distance)
    }

    interface OnLocationMarkerItemClickListener {
        fun onMarkerItemClick(marker: Marker)
    }
}