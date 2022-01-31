package com.iar.core_sample.ui.fragments.arhunts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iar.common.Utils.loadImage
import com.iar.core_sample.R
import com.iar.iar_core.HuntReward

class HuntRewardsAdapter (private val huntRewardList: ArrayList<HuntReward>, val listener : OnHuntRewardItemClickListener) :
    RecyclerView.Adapter<HuntRewardsAdapter.HuntRewardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):HuntRewardViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hunt_reward_item, parent, false)
        return HuntRewardViewHolder(view)
    }

    override fun onBindViewHolder(holder: HuntRewardViewHolder, position: Int) {
        val huntReward = huntRewardList[position]

        huntReward.reward?.image?.url?.let {
            holder.rewardImage.loadImage(it, holder.rewardImage.getContext())
        }

        val IdString = "ID:\n ${huntReward.id}"
        holder.huntRewardId.text = IdString
        val huntIdString = "Hunt Id:\n ${huntReward.huntId}"
        holder.huntRewardHuntId.text = huntIdString

        holder.itemView.setOnClickListener {
            listener.onHuntRewardItemClick (huntReward)
        }
    }

    override fun getItemCount(): Int {
        return huntRewardList.size
    }

    class HuntRewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var huntRewardId = itemView.findViewById<TextView>(R.id.huntReward_id)
        val huntRewardHuntId = itemView.findViewById<TextView>(R.id.huntReward_huntId)
        val rewardImage = itemView.findViewById<ImageView>(R.id.huntReward_image)
    }

    interface OnHuntRewardItemClickListener {
        fun onHuntRewardItemClick(huntReward: HuntReward)
    }
}