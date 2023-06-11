package com.example.habitmanager.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.calendar.repository.CalendarRepository
import com.example.habitmanager.data.user.model.User
import com.example.habitmanager.data.user.repository.UserRepository
import com.example.habitmanagerkt.databinding.CalendarItemBinding
import org.koin.java.KoinJavaComponent.get
import java.util.Calendar

class CalendarAdapter(
    listener: OnItemClickListener
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder?>() {
    private val calendarRepository: CalendarRepository = get(CalendarRepository::class.java)
    var selectedPosition = 0
    private val listener: OnItemClickListener
    private val list: ArrayList<CalendarItem> = ArrayList()

    init {
        this.listener = listener
        list.addAll(calendarRepository.getList())
    }

    fun getItem(position: Int): CalendarItem {
        return list[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CalendarItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                    parent,
                    false
            )
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.weekDayName.text = list[position].weekDay
        holder.binding.weekDatValue.text = list[position].day.toString()
        holder.binding.root.isSelected = position == selectedPosition
    }

    fun selectDay(calendar: Calendar?): Boolean {
        var success = false
        val calendarObject = CalendarItem(calendar!!)
        if (list.contains(calendarObject)) {
            selectedPosition = list.indexOf(calendarObject)
            success = true
            notifyDataSetChanged()
        }
        return success
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var binding: CalendarItemBinding

        init {
            itemView.setOnClickListener(this)
            binding = CalendarItemBinding.bind(itemView)
        }

        override fun onClick(view: View) {
            if (!view.isSelected) {
                view.isSelected = true
            }
            selectedPosition = layoutPosition
            listener.onClick(view, layoutPosition)
            notifyDataSetChanged()
        }
    }

    fun interface OnItemClickListener {
        fun onClick(view: View?, position: Int)
    }
}