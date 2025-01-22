package com.vlg.list

import android.app.Application
import com.vlg.list.room.AppDatabase

class App : Application() {
    val database by lazy {AppDatabase.getDatabase(this)}
}