package com.vlg.list

import android.content.Context
import android.content.SharedPreferences

class SaveConfig(private val context: Context) {
    private lateinit var pref: SharedPreferences

    private val namePref = "list"

    fun saveCurrentGroupId(id: Long) {
        pref = context.getSharedPreferences(namePref, Context.MODE_PRIVATE)
        val ed: SharedPreferences.Editor = pref.edit()
        ed.putLong("group", id)
        ed.apply()
    }

    fun getCurrentGroupId(): Long {
        pref = context.getSharedPreferences(namePref, Context.MODE_PRIVATE)
        return pref.getLong("group", 0)
    }
}