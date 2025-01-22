package com.vlg.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vlg.list.R
import com.vlg.list.model.Group

class AdapterGroup(
) : RecyclerView.Adapter<AdapterGroup.ItemHolder>() {

    var groups: List<Group> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ItemHolder(view: View) : ViewHolder(view) {

        private val name = view.findViewById<TextView>(R.id.nameGroup)

        fun bind(group: Group) {
            name.text = group.nameGroup
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false))

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(groups[position])
    }
}