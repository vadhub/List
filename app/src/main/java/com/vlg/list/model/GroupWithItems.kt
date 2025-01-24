package com.vlg.list.model

import androidx.room.Embedded
import androidx.room.Relation

data class GroupWithItems(
    @Embedded val group: Group,
    @Relation(
        parentColumn = "id_group",
        entityColumn = "group_id"
    )
    val items: List<Item> = emptyList()
)
