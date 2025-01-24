package com.vlg.list

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vlg.list.adapter.AdapterGroup
import com.vlg.list.dialog.SaveGroupDialog
import com.vlg.list.model.Group
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class GroupFragment : Fragment() {

    private var navigator: Navigator = Navigator.Empty()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    val viewModel: GroupViewModel by activityViewModels {
        GroupViewModelFactory(
            (context?.applicationContext as App).database.itemDao(), SaveConfig(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerGroup)
        val buttonAdd = view.findViewById<FloatingActionButton>(R.id.addGroupButton)

        recycler.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterGroup {
            viewModel.currentGroup = it
            navigator.startFragment(SetItemsFragment())
        }
        recycler.adapter = adapter
        getGroupList(adapter)
        buttonAdd.setOnClickListener {
            createDialogSaveGroup { name -> saveGroup(name) }
        }
    }

    fun saveGroup(name: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        viewModel.saveGroup(Group(0, name, formatter.format(date)))
    }

    fun getGroupList(adapter: AdapterGroup) {
        lifecycle.coroutineScope.launch {
            viewModel.getGroupList().collect {
                adapter.groups = it
            }
        }
    }

    fun createDialogSaveGroup(save: (String) -> Unit) {
        val dialogSave = SaveGroupDialog(save)
        dialogSave.show(childFragmentManager, "SaveGroupDialog")
    }
}