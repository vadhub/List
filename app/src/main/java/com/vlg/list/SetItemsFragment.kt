package com.vlg.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vlg.list.adapter.AdapterItem
import com.vlg.list.dialog.SaveItemDialog
import com.vlg.list.model.Group
import com.vlg.list.model.GroupWithItems
import com.vlg.list.model.Item
import kotlinx.coroutines.launch

class SetItemsFragment : Fragment() {

    val viewModel: GroupViewModel by lazy {
        ViewModelProvider(
            this,
            GroupViewModelFactory(
                (context?.applicationContext as App).database.itemDao(),
                SaveConfig(requireContext())
            )
        )[GroupViewModel::class.java]
    }

    var currentGroup = Group.empty

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
        recycler.layoutManager = LinearLayoutManager(context)
        val update: (Item) -> Unit = { viewModel.updateItem(it) }
        val adapter = AdapterItem(update)
        getGroupWithItems(adapter)
        recycler.adapter = adapter
        buttonAdd.setOnClickListener {
            createDialogSaveItem { name, isList -> viewModel.createNewItem(isList, name) }
        }
        groupName.title = currentGroup.nameGroup
    }

    fun getGroupWithItems(adapter: AdapterItem) {
        lifecycle.coroutineScope.launch {
            viewModel.getGroupWithItemsById().collect {
                adapter.items = it.items
                currentGroup = it.group
            }
        }
    }

    fun createDialogSaveItem(save: (String, Boolean) -> Unit) {
        val dialogSave = SaveItemDialog(save)
        dialogSave.show(childFragmentManager, "SaveItemDialog")
    }
}