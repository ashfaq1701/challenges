package com.omise.omisetest.screens.charities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omise.omisetest.databinding.ListItemCharityBinding
import com.omise.omisetest.models.Charity

class CharitiesAdapter(val clickListener: CharityListener): ListAdapter<Charity, CharitiesAdapter.ViewHolder>(CharitiesDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemCharityBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Charity, clickListener: CharityListener) {
            binding.charity = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCharityBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CharitiesDiffCallback : DiffUtil.ItemCallback<Charity>() {
    override fun areItemsTheSame(oldItem: Charity, newItem: Charity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Charity, newItem: Charity): Boolean {
        return oldItem == newItem
    }

}

class CharityListener(val clickListener: (charityId: Int) -> Unit) {
    fun onClick(charity: Charity) = clickListener(charity.id)
}