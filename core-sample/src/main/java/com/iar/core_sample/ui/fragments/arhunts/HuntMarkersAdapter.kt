package com.iar.core_sample.ui.fragments.arhunts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iar.common.Utils.loadImage
import com.iar.core_sample.R
import com.iar.iar_core.HuntMarker

class HuntMarkersAdapter(
    private val huntMarkerList: ArrayList<HuntMarker>,
    val listener: OnHuntMarkerItemClickListener
) :
    RecyclerView.Adapter<HuntMarkersAdapter.HuntMarkerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HuntMarkerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.hunt_marker_item, parent, false)
        return HuntMarkerViewHolder(view)
    }

    override fun onBindViewHolder(holder: HuntMarkerViewHolder, position: Int) {
        val huntMarker = huntMarkerList[position]

        huntMarker.clueCard?.imageUrl?.let {
            holder.clueImage.loadImage(it, holder.clueImage.getContext())
        }

        val IdString = "ID:\n ${huntMarker.id}"
        holder.huntMarkerId.text = IdString
        val huntIdString = "Hunt Id:\n ${huntMarker.huntId}"
        holder.huntMarkerHuntId.text = huntIdString

        holder.itemView.setOnClickListener {
            listener.onHuntMarkerItemClick(huntMarker)
        }
    }

    override fun getItemCount(): Int {
        return huntMarkerList.size
    }

    class HuntMarkerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var huntMarkerId = itemView.findViewById<TextView>(R.id.huntMarker_id)
        val huntMarkerHuntId = itemView.findViewById<TextView>(R.id.huntMarker_huntId)
        val clueImage = itemView.findViewById<ImageView>(R.id.clue_image)
    }

    interface OnHuntMarkerItemClickListener {
        fun onHuntMarkerItemClick(huntMarker: HuntMarker)
    }
}