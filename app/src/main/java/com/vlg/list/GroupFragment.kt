package com.vlg.list

import android.annotation.SuppressLint
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
import com.vlg.list.adapter.AdapterGroup
import com.vlg.list.dialog.SaveGroupDialog
import com.vlg.list.model.Group
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class GroupFragment : Fragment() {

    private var navigator: Navigator = Navigator.Empty()

    val viewModel: GroupViewModel by lazy {
        ViewModelProvider(
            this,
            GroupViewModelFactory(
                (context?.applicationContext as App).database.itemDao(),
                SaveConfig(requireContext())
            )
        )[GroupViewModel::class.java]
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
            navigator.startFragment(SetItemsFragment())
            viewModel.currentGroup = it
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