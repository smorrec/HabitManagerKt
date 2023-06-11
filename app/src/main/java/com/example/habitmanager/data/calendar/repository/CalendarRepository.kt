package com.example.habitmanager.data.calendar.repository

import android.util.Log
import com.example.habitmanager.data.calendar.model.CalendarItem
import java.util.Calendar
import java.util.Date

class CalendarRepository {
    private val list: ArrayList<CalendarItem> = ArrayList()

    init {
        initialize()
    }

    fun getList(): ArrayList<CalendarItem>{
        Log.d("LIS", list.toString())
        return list
    }

    private fun initialize(){
        val calendar = Calendar.getInstance()
        val curdate = Date()
        calendar.time = curdate
        var c = Calendar.getInstance()
        c.timeInMillis = calendar.timeInMillis
        list.add(CalendarItem(c))

        for (i in 1..60){
            calendar.add(Calendar.DATE, 1)
            c = Calendar.getInstance()
            c.timeInMillis = calendar.timeInMillis
            list.add(CalendarItem(c))
        }
    }
}