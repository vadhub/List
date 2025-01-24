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

class GroupViewModel(private val dao: ItemDao, private val saveConfig: SaveConfig) : ViewModel() {

    var currentGroup: Group = Group.empty

    fun getGroupList(): Flow<List<Group>> = dao.getGroups()

    fun getGroupWithItemsById(): Flow<GroupWithItems> = dao.getGroupWithItemsById(saveConfig.getCurrentGroupId())

    fun saveGroupWithItems(groupWithItems: GroupWithItems) = viewModelScope.launch(Dispatchers.IO) {
        dao.saveGroupWithItems(groupWithItems.group, groupWithItems.items)
    }

    fun saveGroup(group: Group) {
        currentGroup = group
        saveConfig.saveCurrentGroupId(dao.insertGroup(group))
    }

    fun saveItem(item: Item) = viewModelScope.launch(Dispatchers.IO) {
        dao.insertItem(item)
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

    fun createNewItem(isList: Boolean, name: String) {
        if (isList) {
            val list = ArrayList<Item>()
            name.split(',').forEach { list.add(Item(0, it, 0, currentGroup.id)) }
            updateGroupWithItems(GroupWithItems(currentGroup, list))
        } else {
            saveItem(Item(0, name, 0, currentGroup.id))
        }
    }
}

class GroupViewModelFactory(private val dao: ItemDao, private val saveConfig: SaveConfig) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GroupViewModel(dao, saveConfig) as T
    }
}