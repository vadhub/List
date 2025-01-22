package com.vlg.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vlg.list.model.Item
import kotlinx.coroutines.launch

class SetItemsFragment : Fragment() {

    val viewModel: GroupViewModel by lazy {
        ViewModelProvider(this, GroupViewModelFactory((context?.applicationContext as App).database.itemDao()))[GroupViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.recycler)
        val buttonAdd = view.findViewById<FloatingActionButton>(R.id.addButton)

        recycler.layoutManager = LinearLayoutManager(context)
        val add: (Item) -> Unit = {}
        val remove: (Item) -> Unit = {}
        val adapter = AdapterItem(add, remove)
        recycler.adapter = adapter

        buttonAdd.setOnClickListener {  }
    }

    fun getGroupWithItems(adapter: AdapterItem, id: Long) {
        lifecycle.coroutineScope.launch {
            viewModel.getGroupWithItemsById(id).collect {
                adapter.items = it.items
            }
        }
    }
}