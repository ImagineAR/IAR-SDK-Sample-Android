package com.iar.core_sample.ui.fragments.rewards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iar.common.Utils.loadImage
import com.iar.core_sample.R
import com.iar.iar_core.Reward


class UserRewardsAdapter(
    private val rewardsList: List<Reward>,
    val listener: OnRewardItemClickListener
) :
    RecyclerView.Adapter<UserRewardsAdapter.RewardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reward_item, parent, false)
        return RewardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val reward = rewardsList[position]

        reward.image?.let {
            holder.rewardImage.loadImage(it.url, holder.rewardImage.getContext())
        }

        holder.rewardName.text = reward.name
        holder.rewardId.text = reward.id
        holder.itemView.setOnClickListener {
            listener.onRewardItemClick(reward)
        }
    }

    override fun getItemCount(): Int {
        return rewardsList.size
    }

    class RewardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rewardName = itemView.findViewById<TextView>(R.id.reward_name)
        val rewardId = itemView.findViewById<TextView>(R.id.reward_id)
        val rewardImage = itemView.findViewById<ImageView>(R.id.reward_image)
    }

    interface OnRewardItemClickListener {
        fun onRewardItemClick(reward: Reward)
    }
}