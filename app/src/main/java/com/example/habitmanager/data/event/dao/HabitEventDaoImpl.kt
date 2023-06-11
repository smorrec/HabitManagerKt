package com.example.habitmanager.data.event.dao

import android.util.Log
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.event.model.HabitEvent
import com.example.habitmanager.data.habit.model.Habit
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class HabitEventDaoImpl(
): HabitEventDao {
    private lateinit var userDbRef: DatabaseReference
    private lateinit var eventDbRef: DatabaseReference
    private lateinit var todayEventsDbRef: DatabaseReference

    override fun prepareDao() {
        userDbRef = Firebase.database.getReference(Firebase.auth.currentUser!!.uid)
        eventDbRef = userDbRef.child("events")

    }

    override fun insert(habitEvent: HabitEvent) {
        eventDbRef.child(habitEvent.calendar!!.fullName)
            .child(habitEvent.habitName!!).setValue(habitEvent)
    }

    override suspend fun getEvent(caledarItem: CalendarItem, habit: Habit): HabitEvent{
        val task = eventDbRef.child(caledarItem.fullName)
            .child(habit.name!!).get()

        task.await()

        return task.result.getValue(HabitEvent::class.java)?: run {
            val event = HabitEvent(habit, caledarItem)
            insert(event)
            event
        }
    }

    override fun update(habitEvent: HabitEvent) {
        eventDbRef
            .child(habitEvent.calendar!!.fullName)
            .child(habitEvent.habitName!!)
            .setValue(habitEvent)
    }

    override fun delete(habitEvent: HabitEvent) {
        eventDbRef
            .child(habitEvent.calendar!!.fullName)
            .child(habitEvent.habitName!!)
            .removeValue()
    }

    override fun deleteAll() {
        eventDbRef
            .removeValue()
    }

    override fun deleteWhereHabit(habitname: String) {
    }

    override fun selectAll(): List<HabitEvent?> {
        return emptyList()
    }
}