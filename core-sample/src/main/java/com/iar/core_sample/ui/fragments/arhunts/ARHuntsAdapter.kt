package com.iar.core_sample.ui.fragments.arhunts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iar.core_sample.R
import com.iar.core_sample.utils.Util.loadImage
import com.iar.iar_core.Hunt

class ARHuntsAdapter(private val huntList: ArrayList<Hunt>, val listener: OnHuntItemClickListener) :
    RecyclerView.Adapter<ARHuntsAdapter.HuntViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HuntViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hunt_item, parent, false)
        return HuntViewHolder(view)
    }

    override fun onBindViewHolder(holder: HuntViewHolder, position: Int) {
        val hunt = huntList[position]

        hunt.thumbnailUrl.let {
            holder.thumbnail.loadImage(it, holder.thumbnail.getContext())
        }

        holder.huntName.text = hunt.name
        holder.huntId.text = hunt.id
        holder.itemView.setOnClickListener {
            listener.onHuntItemClick(hunt)
        }
    }

    override fun getItemCount(): Int {
        return huntList.size
    }

    class HuntViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var huntName = itemView.findViewById<TextView>(R.id.hunt_name)
        val huntId = itemView.findViewById<TextView>(R.id.hunt_id)
        val thumbnail = itemView.findViewById<ImageView>(R.id.hunt_thumbnail)
    }

    interface OnHuntItemClickListener {
        fun onHuntItemClick(hunt: Hunt)
    }

}