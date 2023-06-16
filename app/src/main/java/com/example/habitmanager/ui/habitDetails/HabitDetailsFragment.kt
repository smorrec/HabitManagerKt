package com.example.habitmanager.ui.habitDetails


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.calendar.model.CalendarItem
import com.example.habitmanager.data.category.repository.CategoryRepository
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.data.habit.repository.HabitRepository
import com.example.habitmanager.preferencies.ThemePreferencies
import com.example.habitmanager.utils.toFloat
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentHabitViewBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import java.util.Calendar


class HabitDetailsFragment : Fragment() {
    private var _binding: FragmentHabitViewBinding? = null
    private val binding get() = _binding!!
    private val categoryRepository: CategoryRepository = get(CategoryRepository::class.java)
    private val habitRepository: HabitRepository = get(HabitRepository::class.java)
    private val habitEventRepository: HabitEventRepository = get(HabitEventRepository::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_habit_view, container, false)
        initBinding()
        return binding.root
    }

    private fun initBinding() = HabitManagerApplication.scope().launch {

        binding.habit = requireArguments().getParcelable(Habit.KEY)

        binding.habit!!.calculateDaysCount()
        binding.habit!!.endDateString = binding.habit!!.endDateString?: getString(R.string.noEndDate)

        habitRepository.editHabit(binding.habit!!, binding.habit!!.categoryId!!)

        binding.categoryImageView.setImageResource(
            categoryRepository.getPicture(
                binding.habit!!.categoryId!!
            )
        )

        preparePieChart()
        prepareLineChart()

        binding.categoryTextView.text = categoryRepository.getName(binding.habit!!.categoryId!!)

    }

    private suspend fun prepareLineChart() {
        val data = ArrayList<Entry>()

        val habit = binding.habit!!

        val daysLabels = ArrayList<String>()

        val day = Calendar.getInstance()
        day.timeInMillis = habit.startDate!!
        val calendar = CalendarItem(day)

        var event = habitEventRepository.getEvent(calendar, habit)
        data.add(Entry(0f, event.isCompleted.toFloat()))
        daysLabels.add(event.calendar!!.day.toString()+event.calendar!!.month)

        for(i in 1 until habit.currentDaysCount){
            day.add(Calendar.DATE, 1)
            event = habitEventRepository.getEvent(CalendarItem(day), habit)
            data.add(Entry(i.toFloat(), event.isCompleted.toFloat()))
            daysLabels.add(event.calendar!!.day.toString()+event.calendar!!.month)
        }

        val dataSet = LineDataSet(data, "")

        val xFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                return try{daysLabels[value.toInt()]} catch (e: Exception) {""}
            }
        }

        val yFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return if(value == 1f) getString(R.string.completed) else if(value == 0f) getString(R.string.noCompleted) else ""
            }
        }

        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return ""
            }
        }

        val xAxis: XAxis = binding.lineChart.xAxis
        xAxis.granularity = 1f
        xAxis.valueFormatter = xFormatter

        val yAxis = binding.lineChart.axisLeft
        yAxis.isEnabled = false

        val yAxisR = binding.lineChart.axisRight
        yAxisR.valueFormatter = yFormatter

        var colors: MutableList<Int>?
        val themePref = ThemePreferencies()
        colors = if(themePref.isNightActive()){
            xAxis.textColor = Color.WHITE
            yAxisR.textColor = Color.WHITE
            mutableListOf(requireContext().getColor(R.color.md_theme_light_tertiary),
                requireContext().getColor(R.color.md_theme_light_secondary))
        }else{
            xAxis.textColor = Color.BLACK
            yAxisR.textColor = Color.BLACK
            mutableListOf(requireContext().getColor(R.color.md_theme_dark_tertiary),
                requireContext().getColor(R.color.md_theme_dark_secondary))
        }

        dataSet.colors = colors
        dataSet.valueTextSize = 19F
        val lineData = LineData(dataSet)

        dataSet.valueFormatter = formatter
        dataSet.valueTextSize = 13F
        dataSet.circleRadius = 9F


        binding.lineChart.data = lineData
        binding.lineChart.description = null
        binding.lineChart.legend.textColor = requireContext().getColor(R.color.seed)
        binding.lineChart.axisLeft.setDrawGridLines(false)
        binding.lineChart.xAxis.setDrawGridLines(false)
        binding.lineChart.axisRight.setDrawGridLines(false)
        binding.lineChart.invalidate()
    }

    private fun preparePieChart() {
        val data = ArrayList<PieEntry>()

        val habit = binding.habit!!
        data.add(PieEntry(habit.completedDaysCount.toFloat(),getString(R.string.daysCompleted)))
        data.add(PieEntry(habit.currentDaysCount.toFloat() - habit.completedDaysCount.toFloat(), getString(R.string.daysNotCompleted)))

        val pieDataSet = PieDataSet(data, getString(R.string.daysCompleted))

        var colors: MutableList<Int>?
        val themePref = ThemePreferencies()
        colors = if(themePref.isNightActive()){
            pieDataSet.valueTextColor = Color.WHITE
            binding.pieChart.setEntryLabelColor(Color.WHITE)
            binding.pieChart.legend.textColor = Color.WHITE
            mutableListOf(requireContext().getColor(R.color.md_theme_light_tertiary),
                requireContext().getColor(R.color.md_theme_light_secondary))
        }else{
            pieDataSet.valueTextColor = Color.BLACK
            binding.pieChart.setEntryLabelColor(Color.BLACK)
            binding.pieChart.legend.textColor = Color.BLACK
            mutableListOf(requireContext().getColor(R.color.md_theme_dark_tertiary),
                requireContext().getColor(R.color.md_theme_dark_secondary))
        }

        pieDataSet.colors = colors
        pieDataSet.valueTextSize = 19F

        val pieData = PieData(pieDataSet)

        val formatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }

        pieData.setValueFormatter(formatter)

        binding.pieChart.data = pieData
        binding.pieChart.description = null
        binding.pieChart.setTransparentCircleColor(android.R.color.transparent)
        binding.pieChart.setHoleColor(android.R.color.transparent)
        binding.pieChart.setEntryLabelTextSize(19F)
        binding.pieChart.invalidate()
    }
}