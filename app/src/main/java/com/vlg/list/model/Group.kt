package com.vlg.list.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("group")
data class Group(
    @PrimaryKey
    @ColumnInfo("id_group")
    val id: Long,
    @ColumnInfo("name_group")
    val nameGroup: String,
    val dateCreated: String,
) {
    companion object {
        val empty = Group(-1, "", "")
    }
}
