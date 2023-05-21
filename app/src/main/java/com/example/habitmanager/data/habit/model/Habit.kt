package com.example.habitmanager.data.habit.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.category.model.Category
import java.util.Calendar
import java.util.Objects

@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["category_id"],
        onDelete = 5)]
)
class Habit : Parcelable, Comparable<Habit?> {
    @PrimaryKey
    @ColumnInfo(name = "habitName")
    var name: String? = null
    var description: String? = null
    var startDate: Calendar? = null
    var startDateString: String? = null
    var endDate: Calendar? = null
    var endDateString: String? = null
    @ColumnInfo(name = "category_id")
    var categoryId = 0
    var currentDaysCount: Int
    var completedDaysCount: Int
    var isFinished = false


    constructor() {
        currentDaysCount = 0
        completedDaysCount = 0
    }

    constructor(
        name: String,
        description: String?,
        startDate: Calendar,
        endDate: Calendar?,
        categoryId: Int
    ) {
        this.name = name
        this.description = description
        this.startDate = startDate
        this.endDate = endDate
        this.categoryId = categoryId
        currentDaysCount = 0
        completedDaysCount = 0
    }

    constructor(habit: Habit) {
        name = habit.name
        description = habit.description
        startDate = habit.startDate
        startDateString = habit.startDateString
        endDate = habit.endDate
        endDateString = habit.endDateString
        categoryId = habit.categoryId
        currentDaysCount = habit.currentDaysCount
        completedDaysCount = habit.completedDaysCount
    }

    fun clone(): Habit {
        return Habit(this)
    }

    protected constructor(`in`: Parcel) {
        name = `in`.readString()!!
        description = `in`.readString()
        currentDaysCount = `in`.readInt()
        completedDaysCount = `in`.readInt()
    }

    fun increaseCurrentDaysCount() {
        currentDaysCount++
    }

    fun increaseCompletedDaysCount() {
        completedDaysCount++
    }

    fun decreaseCompletedDaysCount() {
        completedDaysCount--
    }

    override fun compareTo(other: Habit?): Int {
        return name!!.compareTo(other!!.name!!, ignoreCase = true)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val habit = o as Habit
        return name == habit.name
    }

    override fun hashCode(): Int {
        return Objects.hash(
            name,
            description,
            startDate,
            endDate,
            categoryId,
            currentDaysCount,
            completedDaysCount
        )
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(currentDaysCount)
        parcel.writeInt(completedDaysCount)
    }

    fun hasTask(calendarItem: CalendarItem): Boolean {
        Log.d("startDate", startDate!!.timeInMillis.toString())
        Log.d(
            "calendarObject",
            java.lang.String.valueOf(calendarItem.calendar.getTimeInMillis())
        )
        Log.d(
            "has task",
            (startDate!!.timeInMillis <= calendarItem.calendar.getTimeInMillis()).toString()
        )
        return startDate!!.timeInMillis <= calendarItem.calendar.getTimeInMillis()
    }

    companion object CREATOR : Creator<Habit> {
        override fun createFromParcel(parcel: Parcel): Habit {
            return Habit(parcel)
        }

        override fun newArray(size: Int): Array<Habit?> {
            return arrayOfNulls(size)
        }

        const val KEY = "habit"
    }
}