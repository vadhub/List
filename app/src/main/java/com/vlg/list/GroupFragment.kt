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
import com.vlg.list.adapter.AdapterGroup
import com.vlg.list.adapter.AdapterItem
import com.vlg.list.model.Item
import kotlinx.coroutines.launch

class GroupFragment : Fragment() {

    val viewModel: GroupViewModel by lazy {
        ViewModelProvider(this, GroupViewModelFactory((context?.applicationContext as App).database.itemDao()))[GroupViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerGroup)
        val buttonAdd = view.findViewById<FloatingActionButton>(R.id.addGroupButton)

        recycler.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterGroup()
        recycler.adapter = adapter
        getGroupList(adapter)
        buttonAdd.setOnClickListener {  }
    }

    fun getGroupList(adapter: AdapterGroup) {
        lifecycle.coroutineScope.launch {
            viewModel.getGroupList().collect {
                adapter.groups = it
            }
        }
    }
}