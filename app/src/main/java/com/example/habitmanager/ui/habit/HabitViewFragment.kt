package com.example.habitmanager.ui.habit


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.category.repository.CategoryRepository
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.data.habit.repository.HabitRepository
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentHabitViewBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class HabitViewFragment : Fragment() {
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

        habitRepository.editHabit(binding.habit!!, binding.habit!!.categoryId!!)

        binding.categoryImageView.setImageResource(
            categoryRepository.getPicture(
                binding.habit!!.categoryId!!
            )
        )
        binding.categoryTextView.text = categoryRepository.getName(binding.habit!!.categoryId!!)
        val habit = binding.habit!!

        val data = ArrayList<PieEntry>()


        data.add(PieEntry(habit.completedDaysCount.toFloat(),"Días completados"))
        data.add(PieEntry(habit.currentDaysCount.toFloat() - habit.completedDaysCount.toFloat(),"Días sin completar"))

        val pieDataSet = PieDataSet(data, "Días completados")
        val colors = mutableListOf(requireContext().getColor(R.color.md_theme_dark_tertiary), requireContext().getColor(R.color.md_theme_dark_secondary))
        pieDataSet.colors = colors
        pieDataSet.valueTextSize = 19F
        val pieData = PieData(pieDataSet)
        pieData.setValueFormatter(PercentFormatter())

        binding.pieChart.data = pieData
        binding.pieChart.description = null
        binding.pieChart.setTransparentCircleColor(android.R.color.transparent)
        binding.pieChart.setHoleColor(android.R.color.transparent)
        binding.pieChart.setEntryLabelTextSize(19F)
        binding.pieChart.setEntryLabelColor(requireContext().getColor(R.color.seed))
        binding.pieChart.legend.textColor = requireContext().getColor(R.color.seed)
        binding.pieChart.invalidate()
    }

    private fun goBack() {
        NavHostFragment.findNavController(this).navigateUp()
    }
}