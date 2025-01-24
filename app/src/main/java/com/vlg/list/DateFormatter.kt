package com.vlg.list

import java.text.SimpleDateFormat
import java.util.Date

class DateFormatter {
    companion object {
        private val formatter = SimpleDateFormat("yyyy-MM-dd")
        private val date = Date()

        fun getDate(): String {
            return formatter.format(date)
        }
    }
}