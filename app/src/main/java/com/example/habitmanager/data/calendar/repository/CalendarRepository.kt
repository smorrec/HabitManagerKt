package com.example.habitmanager.data.calendar.repository

import com.example.habitmanager.data.calendar.model.CalendarItem
import java.util.Calendar
import java.util.Date

class CalendarRepository {
    private lateinit var list: ArrayList<CalendarItem>

    init {
        initialize()
    }

    fun getList(): ArrayList<CalendarItem>{
        return list
    }

    private fun initialize(){
        list = ArrayList()
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