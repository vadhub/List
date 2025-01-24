package com.vlg.list.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id_item")
    val id: Long,
    val name: String,
    var count: Int,
    @ColumnInfo("group_id")
    var groupId: Long,
    var dataChange: String,
)
