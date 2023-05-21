package com.example.habitmanager.data.habit.comparator

import com.example.habitmanager.data.habit.model.Habit

class HabitComparatorByCategory: Comparator<Habit> {
    override fun compare(o1: Habit?, o2: Habit?): Int {
        return Integer.compare(o1!!.categoryId, o2!!.categoryId)
    }
}