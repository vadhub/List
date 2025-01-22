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
import com.vlg.list.adapter.AdapterItem
import com.vlg.list.dialog.SaveItemDialog
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

        val idGroup = arguments?.getLong("groupId") ?: 0

        recycler.layoutManager = LinearLayoutManager(context)
        val update: (Item) -> Unit = {viewModel.updateItem(it)}
        val adapter = AdapterItem(update)
        getGroupWithItems(adapter, idGroup)
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

    fun createDialogSaveItem(save: (String, Boolean) -> Unit) {
        val dialogSave = SaveItemDialog(save)
        dialogSave.show(childFragmentManager, "SaveItemDialog")
    }
}