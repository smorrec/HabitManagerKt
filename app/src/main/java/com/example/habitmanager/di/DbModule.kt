package com.example.habitmanager.di
import com.example.habitmanager.data.category.dao.CategoryDao
import com.example.habitmanager.data.category.dao.CategoryDaoImpl
import com.example.habitmanager.data.event.dao.HabitEventDao
import com.example.habitmanager.data.event.dao.HabitEventDaoImpl
import com.example.habitmanager.data.habit.dao.HabitDao
import com.example.habitmanager.data.habit.dao.HabitDaoImpl
import com.example.habitmanager.data.user.dao.UserDao
import com.example.habitmanager.data.user.dao.UserDaoImpl
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.dsl.bind
import org.koin.dsl.module

val dbModule = module {
    single { CategoryDaoImpl() } bind CategoryDao::class
    single { HabitDaoImpl() } bind HabitDao::class
    single { HabitEventDaoImpl() } bind HabitEventDao::class
    single { UserDaoImpl() } bind UserDao::class

}