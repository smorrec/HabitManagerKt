package com.example.habitmanager.di

import androidx.room.Room
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.category.dao.CategoryDao
import com.example.habitmanager.database.Database
import org.koin.dsl.module

val dbModule = module {
    single {
        Room.databaseBuilder(
        HabitManagerApplication.applicationContext(),
        Database::class.java,
        "habitmanager-database"
        ).build()
    }

    single { get<Database>().categoryDao() }
    single { get<Database>().habitDao() }
    single { get<Database>().habitEventDao() }

}