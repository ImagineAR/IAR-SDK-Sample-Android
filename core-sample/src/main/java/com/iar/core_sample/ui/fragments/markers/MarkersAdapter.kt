package com.iar.core_sample.ui.fragments.markers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iar.core_sample.R
import com.iar.core_sample.utils.Util.loadImage
import com.iar.iar_core.Marker


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

        marker.previewImageUrl?.let {
            holder.markerImage.loadImage(it, holder.markerImage.getContext())
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