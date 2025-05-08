package com.vlg.list.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vlg.list.R
import com.vlg.list.model.Group

class AdapterGroup(
    private val clickOnViewListener: (Group) -> Unit,
    private val onEditClick: (Group) -> Unit,
    private val onDeleteClick: (Group) -> Unit,
    private val onExportClick: (Group) -> Unit
) : RecyclerView.Adapter<AdapterGroup.ItemHolder>() {

    var groups: List<Group> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ItemHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.nameGroup)
        private val actionMenu = view.findViewById<ImageButton>(R.id.actionMenu)

        fun bind(group: Group) {
            name.text = group.nameGroup
            view.setOnClickListener { clickOnViewListener.invoke(group) }

            actionMenu.setOnClickListener { v ->
                showPopupMenu(v, group)
            }
        }

        private fun showPopupMenu(view: View, group: Group) {
            val popup = PopupMenu(view.context, view)
            popup.menuInflater.inflate(R.menu.menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        onEditClick(group)
                        true
                    }
                    R.id.action_delete -> {
                        onDeleteClick(group)
                        true
                    }
                    R.id.action_export -> {
                        onExportClick(group)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false))

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(groups[position])
    }
}