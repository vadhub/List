package com.vlg.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.vlg.list.R
import com.vlg.list.model.Item

class AdapterItem(
    private val update: (item: Item) -> Unit
) : RecyclerView.Adapter<AdapterItem.ItemHolder>() {

    var items: List<Item> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ItemHolder(view: View) : ViewHolder(view) {

        private val name = view.findViewById<TextView>(R.id.name)
        private val count = view.findViewById<TextView>(R.id.count)
        private val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
        private val buttonRemove = view.findViewById<Button>(R.id.buttonRemove)

        fun bind(item: Item) {
            count.text = item.count.toString()
            name.text = item.name
            buttonAdd.setOnClickListener {
                item.count += 1
                count.text = item.count.toString()
                update.invoke(item)
            }
            buttonRemove.setOnClickListener {
                item.count -= 1
                count.text = item.count.toString()
                update.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(items[position])
    }
}