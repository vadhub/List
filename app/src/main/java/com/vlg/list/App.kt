package com.vlg.list

import android.app.Application
import com.vlg.list.room.AppDatabase

class App : Application() {
    val database by lazy {AppDatabase.getDatabase(this)}
}


// TODO
// Пофиксить один чел во всех группах
// Экспорт в csv добавить