package com.example.habitmanager.di

import com.example.habitmanager.data.calendar.repository.CalendarRepository
import com.example.habitmanager.data.category.repository.CategoryRepository
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.repository.HabitRepository
import com.example.habitmanager.data.user.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { CategoryRepository(get()) }
    single { CalendarRepository() }
    single { HabitRepository(get()) }
    single { HabitEventRepository(get()) }
    single { UserRepository() }
}