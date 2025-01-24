package com.vlg.list.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vlg.list.App
import com.vlg.list.DateFormatter
import com.vlg.list.Navigator
import com.vlg.list.R
import com.vlg.list.SaveConfig
import com.vlg.list.ui.adapter.AdapterGroup
import com.vlg.list.ui.dialog.SaveGroupDialog
import com.vlg.list.model.Group
import kotlinx.coroutines.launch

class GroupFragment : BaseFragment() {

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
            createDialogSaveGroup { name ->
                viewModel.saveGroup(Group(0, name, DateFormatter.getDate()))
            }
        }
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