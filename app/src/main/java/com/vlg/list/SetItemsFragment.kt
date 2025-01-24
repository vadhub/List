package com.vlg.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vlg.list.adapter.AdapterItem
import com.vlg.list.dialog.SaveItemDialog
import com.vlg.list.model.Group
import com.vlg.list.model.Item
import kotlinx.coroutines.launch

class SetItemsFragment : Fragment() {

    private var navigator: Navigator = Navigator.Empty()

    val viewModel: GroupViewModel by activityViewModels {
        GroupViewModelFactory(
            (context?.applicationContext as App).database.itemDao(), SaveConfig(requireContext())
        )
    }

    val currentGroup by lazy { viewModel.currentGroup }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
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
        val groupName = view.findViewById<Toolbar>(R.id.toolbar)
        val menu = view.findViewById<ImageView>(R.id.menu)
        recycler.layoutManager = LinearLayoutManager(context)
        val update: (Item) -> Unit = { viewModel.updateItem(it) }
        val adapter = AdapterItem(update)
        getGroupWithItems(adapter)
        recycler.adapter = adapter
        buttonAdd.setOnClickListener {
            createDialogSaveItem { name, isList -> viewModel.createNewItem(isList, name) }
        }
        menu.setOnClickListener { navigator.startFragment(GroupFragment()) }
        groupName.title = currentGroup.nameGroup
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