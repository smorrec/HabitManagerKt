package com.example.habitmanager.ui.habit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.category.repository.CategoryRepository
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanagerkt.databinding.FragmentHabitViewBinding
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class HabitViewFragment : Fragment() {
    private var binding: FragmentHabitViewBinding? = null
    private val categoryRepository: CategoryRepository = get(CategoryRepository::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initBinding(inflater)
        return binding!!.root
    }

    private fun initBinding(inflater: LayoutInflater) = HabitManagerApplication.scope().launch {
        binding = FragmentHabitViewBinding.inflate(inflater)
        binding!!.habit = requireArguments().getParcelable(Habit.KEY)
        binding!!.fab.setOnClickListener { _ : View? -> goBack() }
        binding!!.categoryImageView.setImageResource(
            categoryRepository.getPicture(
                binding!!.habit!!.categoryId
            )
        )
        binding!!.categoryTextView.text = categoryRepository.getName(binding!!.habit!!.categoryId)
    }

    private fun goBack() {
        NavHostFragment.findNavController(this).navigateUp()
    }
}