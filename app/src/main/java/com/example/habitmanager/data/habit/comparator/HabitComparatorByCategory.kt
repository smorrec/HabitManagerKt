package com.example.habitmanager.data.habit.comparator

import com.example.habitmanager.data.habit.model.Habit

class HabitComparatorByCategory: Comparator<Habit> {
    override fun compare(o1: Habit?, o2: Habit?): Int {
        return (o1!!.categoryId!!).compareTo(o2!!.categoryId!!)
    }
}