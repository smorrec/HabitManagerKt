package com.example.habitmanager.utils

import androidx.room.TypeConverter
import java.util.Calendar

class CalendarConverter {
    companion object{
        @TypeConverter
        fun fromTimeStamp(value: Long?): Calendar?{
            value.let {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = it!!
                return calendar
            }
        }

        @TypeConverter
        fun toTimeStamp(calendar: Calendar): Long{
            return calendar.timeInMillis
        }
    }
}