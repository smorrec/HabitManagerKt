package com.example.habitmanager.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitmanager.adapter.CalendarAdapter
import com.example.habitmanager.adapter.EventsAdapter
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.event.dao.HabitEventDao
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.dao.HabitDao
import com.example.habitmanager.data.habit.repository.HabitRepository
import com.example.habitmanager.ui.MainActivity
import com.example.habitmanager.ui.base.BaseFragment
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentMainBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.get
import java.util.Calendar

class MainFragment : BaseFragment(), CalendarAdapter.OnItemClickListener {
    private var binding: FragmentMainBinding? = null
    private var calendarAdapter: CalendarAdapter? = null
    private var eventAdapter: EventsAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        prepareDaos()
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun prepareDaos() {
        val habitDao: HabitDao = get(HabitDao::class.java)
        habitDao.prepareDao()
        val habitEventDao: HabitEventDao = get(HabitEventDao::class.java)
        habitEventDao.prepareDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        initRvCalendar()
        initRvTasks()

        requireActivity().findViewById<View>(R.id.bottom_navigation).visibility = View.VISIBLE
        (requireActivity().findViewById<View>(R.id.progressBar) as LinearProgressIndicator).show()

        lifecycleScope.launch {
            eventAdapter!!.setSelectedCalendar(calendarAdapter!!.getItem(0))
            (requireActivity().findViewById<View>(R.id.progressBar) as LinearProgressIndicator).hide()
            binding!!.taskList.startLayoutAnimation()
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
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
                lifecycleScope.launch {
                    eventAdapter!!.setSelectedCalendar(CalendarItem(calendar))
                }

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
        val progressBar = (requireActivity().findViewById<View>(R.id.progressBar) as LinearProgressIndicator)
        progressBar.show()
        lifecycleScope.launch {
            requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            eventAdapter!!.setSelectedCalendar(calendarAdapter!!.getItem(position))
            progressBar.hide()
            binding!!.taskList.startLayoutAnimation()
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.show()
        requireActivity().findViewById<View>(R.id.bottom_navigation).visibility = View.VISIBLE
    }
}