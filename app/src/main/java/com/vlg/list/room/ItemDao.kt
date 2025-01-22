package com.vlg.list.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.vlg.list.model.Group
import com.vlg.list.model.GroupWithItems
import com.vlg.list.model.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: Item)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: Item)

    @Insert
    suspend fun insertGroup(group: Group): Long

    @Update
    fun updateGroup(group: Group)

    @Delete
    fun deleteGroup(group: Group)

    @Transaction
    @Query("SELECT * FROM `group`")
    fun getGroupWithItems(): Flow<List<GroupWithItems>>

    @Query("SELECT * FROM `group`")
    fun getGroup(): Flow<List<Group>>

    @Transaction
    @Query("SELECT * FROM `group` WHERE id_group = :groupId")
    fun getGroupWithItemsById(groupId: Long): Flow<GroupWithItems>

    @Transaction
    suspend fun saveGroupWithItems(group: Group, items: List<Item>) {
        val groupId = insertGroup(group)
        items.forEach {
            it.groupId = groupId
            insertItem(it)
        }
    }

    @Transaction
    suspend fun updateGroupWithItems(group: Group, items: List<Item>) {
        updateGroup(group)
        items.forEach { item ->
            item.groupId = group.id
            updateItem(item)
        }
    }

    @Query("DELETE FROM item WHERE group_id = :groupId")
    fun deleteItemById(groupId: Long)

    @Transaction
    fun deleteGroupWithItems(group: Group) {
        deleteGroup(group)
        deleteItemById(group.id)
    }
}