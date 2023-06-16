package com.example.habitmanager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.category.repository.CategoryRepository
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.repository.HabitRepository
import com.example.habitmanager.data.event.model.HabitEvent
import com.example.habitmanagerkt.databinding.ItemTaskBinding
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.get
import java.util.Calendar

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder?>() {
    private val categoryRepository: CategoryRepository =
        get(CategoryRepository::class.java)
    private val habitRepository: HabitRepository =
        get(HabitRepository::class.java)
    private val listToShow: ArrayList<HabitEvent> = ArrayList()
    private var selectedCalendar: CalendarItem

    init {
        selectedCalendar = CalendarItem(Calendar.getInstance())
    }

    fun setSelectedCalendar(calendar: CalendarItem, events: ArrayList<HabitEvent>) {
        selectedCalendar = calendar
        listToShow.clear()
        listToShow.addAll(events)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return listToShow.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.event = listToShow[position]
        holder.binding.avatarImageView2.setImageResource(
            runBlocking {
                categoryRepository.getPicture(
                    habitRepository.selectByName(
                        listToShow[position].habitName!!
                    )!!.categoryId!!
                )
            }

        )
        holder.binding.textView.text = listToShow[position].habitName
        holder.binding.descriptionnBtn.isChecked = listToShow[position].isCompleted
        holder.binding.descriptionnBtn.isEnabled = listToShow[position].isCurrentDay()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemTaskBinding
        var event: HabitEvent? = null
        private val habitEventRepository: HabitEventRepository = get(HabitEventRepository::class.java)
        private val habitRepository: HabitRepository = get(HabitRepository::class.java)

        init {
            binding = ItemTaskBinding.bind(itemView)
            binding.descriptionnBtn.
            addOnCheckedStateChangedListener { checkBox: MaterialCheckBox, _: Int ->
                if(event!!.isCurrentDay()) {
                    val initialState = event!!.isCompleted
                    event!!.isCompleted = checkBox.isChecked
                    habitEventRepository.update(event!!)
                    if (initialState != event!!.isCompleted)
                        habitRepository.modifiedCompletedDays(event!!)
                }
            }
        }
    }
}