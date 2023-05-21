package com.example.habitmanager.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitmanager.adapter.CalendarAdapter
import com.example.habitmanager.adapter.EventsAdapter
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.ui.base.BaseFragment
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import java.util.Calendar

class MainFragment : BaseFragment(), CalendarAdapter.OnItemClickListener {
    private var binding: FragmentMainBinding? = null
    private var calendarAdapter: CalendarAdapter? = null
    private var eventAdapter: EventsAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        initRvCalendar()
        initRvTasks()
        requireActivity().findViewById<View>(R.id.bottom_navigation).setVisibility(View.VISIBLE)
        return binding!!.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.menu_calendar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_selectDay -> {
                selectCalendar()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectCalendar() {
        val picker: MaterialDatePicker<Long> = MaterialDatePicker.Builder.datePicker().build()
        picker.addOnPositiveButtonClickListener { selection: Long? ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selection!!
            if (calendarAdapter!!.selectDay(calendar)) {
                linearLayoutManager!!.scrollToPositionWithOffset(
                    calendarAdapter!!.selectedPosition,
                    getResources().getDimension(R.dimen.offset).toInt()
                )
                eventAdapter!!.setSelectedCalendar(CalendarItem(calendar))
            }
        }
        picker.show(requireActivity().supportFragmentManager, "datePicker")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initRvCalendar() {
        calendarAdapter = CalendarAdapter(this)
        linearLayoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
        binding!!.calendarList.layoutManager = linearLayoutManager
        binding!!.calendarList.adapter = calendarAdapter
    }

    private fun initRvTasks() {
        eventAdapter = EventsAdapter()
        val linearLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding!!.taskList.layoutManager = linearLayoutManager
        binding!!.taskList.adapter = eventAdapter
    }

    override fun onClick(view: View?, position: Int) {
        eventAdapter!!.setSelectedCalendar(calendarAdapter!!.getItem(position))
        binding!!.taskList.startLayoutAnimation()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.show()
        requireActivity().findViewById<View>(R.id.bottom_navigation).visibility = View.VISIBLE
    }
}