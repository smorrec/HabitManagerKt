package com.example.habitmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.category.repository.CategoryRepository
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.repository.HabitRepository
import com.example.habitmanager.data.task.model.HabitEvent
import com.example.habitmanagerkt.databinding.ItemTaskBinding
import com.google.android.material.checkbox.MaterialCheckBox
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.get
import java.util.Calendar

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.ViewHolder?>() {
    private val list: ArrayList<HabitEvent?>
    private val listToShow: ArrayList<HabitEvent> = ArrayList()
    private var selectedCalendar: CalendarItem
    private val habitEventRepository: HabitEventRepository = get(HabitEventRepository::class.java)
    private val categoryRepository: CategoryRepository = get(CategoryRepository::class.java)
    private val habitRepository: HabitRepository = get(HabitRepository::class.java)

    init {
        list = getRepositoryList()
        selectedCalendar = CalendarItem(Calendar.getInstance())
        fillList()
    }

    private fun getRepositoryList(): ArrayList<HabitEvent?> {
        val list = runBlocking {
            habitEventRepository.getList()
        }
        return list
    }

    fun setSelectedCalendar(calendar: CalendarItem) {
        selectedCalendar = calendar
        listToShow.clear()
        fillList()
        notifyDataSetChanged()
    }

    private fun fillList() {
        for (habitEvent in list) {
            if (habitEvent!!.calendar!! == selectedCalendar) {
                listToShow.add(habitEvent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false)
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
                        listToShow[position].habitName
                    ).categoryId
                )
            }

        )
        holder.binding.textView.setText(listToShow[position].habitName)
        holder.binding.descriptionnBtn.isChecked = listToShow[position].isCompleted
        holder.binding.descriptionnBtn.isEnabled = listToShow[position].isCurrentDay
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemTaskBinding
        var event: HabitEvent? = null
        val habitEventRepository: HabitEventRepository = get(HabitEventRepository::class.java)

        init {
            binding = ItemTaskBinding.bind(itemView)
            binding.descriptionnBtn.
            addOnCheckedStateChangedListener { checkBox: MaterialCheckBox, state: Int ->
                event!!.isCompleted = checkBox.isChecked
                habitEventRepository.backgroundJob {
                    habitEventRepository.update(event)
                }
            }
        }
    }
}