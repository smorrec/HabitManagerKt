package com.example.habitmanager.database

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.habitmanager.data.category.dao.CategoryDao
import com.example.habitmanager.data.category.model.Category
import com.example.habitmanager.data.event.dao.HabitEventDao
import com.example.habitmanager.data.habit.dao.HabitDao
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.data.task.model.HabitEvent
import com.example.habitmanager.utils.CalendarConverter

@androidx.room.Database(entities = [Habit::class, Category::class, HabitEvent::class] , version = 1)
@TypeConverters(CalendarConverter::class)
abstract class Database: RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun habitEventDao(): HabitEventDao
    abstract fun categoryDao(): CategoryDao
}