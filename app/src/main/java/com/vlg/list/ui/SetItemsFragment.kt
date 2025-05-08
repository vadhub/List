package com.vlg.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vlg.list.R
import com.vlg.list.model.Item
import com.vlg.list.ui.adapter.AdapterItem
import com.vlg.list.ui.dialog.SaveItemDialog
import kotlinx.coroutines.launch

class SetItemsFragment : BaseFragment() {
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
        val groupName = view.findViewById<Toolbar>(R.id.toolbar)
        val menu = view.findViewById<ImageView>(R.id.menu)
        val update: (Item) -> Unit = { viewModel.updateItem(it) }
        val adapter = AdapterItem(update)

        recycler.layoutManager = LinearLayoutManager(context)
        getGroupWithItems(adapter)
        recycler.adapter = adapter
        buttonAdd.setOnClickListener {
            createDialogSaveItem { name, isList -> viewModel.createNewItem(isList, name) }
        }
        menu.setOnClickListener { navigator.startFragment(GroupFragment()) }
        groupName.title = viewModel.currentGroup.nameGroup
    }

    fun getGroupWithItems(adapter: AdapterItem) {
        lifecycle.coroutineScope.launch {
            viewModel.getGroupWithItemsById().collect {
                adapter.items = it.items
            }
        }
    }

    fun createDialogSaveItem(save: (String, Boolean) -> Unit) {
        val dialogSave = SaveItemDialog(save)
        dialogSave.show(childFragmentManager, "SaveItemDialog")
    }
}