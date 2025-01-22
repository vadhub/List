package com.vlg.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vlg.list.model.Group
import com.vlg.list.model.GroupWithItems
import com.vlg.list.model.Item
import com.vlg.list.room.ItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class GroupViewModel(private val dao: ItemDao) : ViewModel() {

    fun getGroupWithItems(): Flow<List<GroupWithItems>> = dao.getGroupWithItems()

    fun getGroupList(): Flow<List<Group>> = dao.getGroup()

    fun getGroupWithItemsById(groupId: Long): Flow<GroupWithItems> = dao.getGroupWithItemsById(groupId)

    fun saveGroupWithItems(groupWithItems: GroupWithItems) = viewModelScope.launch(Dispatchers.IO) {
        dao.saveGroupWithItems(groupWithItems.group, groupWithItems.items)
    }

    fun updateGroupWithItems(groupWithItems: GroupWithItems) = viewModelScope.launch(Dispatchers.IO) {
        dao.updateGroupWithItems(groupWithItems.group, groupWithItems.items)
    }

    fun updateItem(item: Item) = viewModelScope.launch(Dispatchers.IO) {
        dao.updateItem(item)
    }

    fun deleteGroupWithItems(group: Group) = viewModelScope.launch(Dispatchers.IO) {
        dao.deleteGroupWithItems(group)
    }
}

class GroupViewModelFactory(private val dao: ItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GroupViewModel(dao) as T
    }
}