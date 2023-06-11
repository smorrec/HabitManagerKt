package com.example.habitmanager.ui.habit

import android.Manifest
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.NavHostFragment
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.adapter.CategoryAdapter
import com.example.habitmanager.data.category.model.Category
import com.example.habitmanager.data.category.repository.CategoryRepository
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.preferencies.NotificationPreferencies
import com.example.habitmanager.ui.base.BaseFragment
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentAddEditHabitBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import java.util.Calendar

class HabitManagerFragment : BaseFragment() {
    private var binding: FragmentAddEditHabitBinding? = null
    private var viewModel: HabitManagerViewModel? = null
    private var adapter: CategoryAdapter? = null
    private val categoryRepository: CategoryRepository = get(CategoryRepository::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditHabitBinding.inflate(inflater)
        prepareSpinner()
        val habit = Habit()
        binding!!.habit = habit
        return binding!!.root
    }

    private fun prepareSpinner() = HabitManagerApplication.scope().launch {
        adapter = CategoryAdapter(
            requireContext(),
            R.layout.item_category,
            categoryRepository.getList()
        )
        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding!!.CategorySpinner.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.txtStartDatePicker.setOnClickListener {
            showDatePickerDialog(
                binding!!.txtStartDatePicker
            )
        }
        binding!!.txtEndDatePicker.setOnClickListener {
            showDatePickerDialog(
                binding!!.txtEndDatePicker
            )
        }
        binding!!.fab.setOnClickListener { _: View? ->
            viewModel!!.addHabit(
                binding!!.habit!!,
                (binding!!.CategorySpinner.selectedItem as Category).id!!
            )
        }
        binding!!.txtHabitName.addTextChangedListener(HabitTextWatcher(binding!!.txtHabitName))
        binding!!.txtStartDatePicker.addTextChangedListener(HabitTextWatcher(binding!!.txtStartDatePicker))
        if (arguments != null) {
            showEdit()

        }
        initViewModel()
    }

    fun showDatePickerDialog(editText: TextInputEditText) {
        val builder: CalendarConstraints.Builder = CalendarConstraints.Builder()
        val picker: MaterialDatePicker<Long> = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(builder.setValidator(DateValidatorPointForward.now()).build())
            .build()
        picker.addOnPositiveButtonClickListener { selection: Long? ->
            editText.setText(picker.headerText)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selection!!
            when (editText.id) {
                R.id.txtStartDatePicker -> binding!!.habit!!.startDate = calendar.timeInMillis
                R.id.txtEndDatePicker -> binding!!.habit!!.endDate = calendar.timeInMillis
            }
        }
        picker.show(requireActivity().supportFragmentManager, "datePicker")
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(HabitManagerViewModel::class.java)
        viewModel!!.resultMutableLiveData.observe(viewLifecycleOwner) { habitManagerResult: HabitManagerResult? ->
            when (habitManagerResult) {
                HabitManagerResult.NAMEEMPTY -> {
                    binding!!.txtHabitNameLayout.error = getString(R.string.errorNameEmpty)
                    binding!!.txtHabitNameLayout.requestFocus()
                }

                HabitManagerResult.STARTDATEEMPTY -> {
                    binding!!.txtStartDateLayout.error = getString(R.string.errorStartDateEmpty)
                    binding!!.txtStartDateLayout.requestFocus()
                }

                HabitManagerResult.FAILURE -> {
                    binding!!.txtHabitNameLayout.error = getString(R.string.errorDuplicateHabit)
                    binding!!.txtHabitNameLayout.requestFocus()
                }

                HabitManagerResult.SUCCESS -> {
                    NavHostFragment.findNavController(this).navigateUp()
                    if (NotificationPreferencies().isActive()) {
                        showAddNotification(binding!!.habit!!.name)
                    }
                }

                else -> {}
            }
        }
    }

    private fun showAddNotification(name: String?) {
        val bundle = Bundle()
        bundle.putParcelable(Habit.KEY, binding!!.habit)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pendingIntent: PendingIntent = NavDeepLinkBuilder(requireContext())
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.habitListFragment)
                .setArguments(bundle)
                .createPendingIntent()
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(requireContext(), HabitManagerApplication.channelId())
                    .setSmallIcon(R.drawable.splashicon)
                    .setContentTitle(getString(R.string.addHabitNotTitle))
                    .setContentText(getString(R.string.addHabitNotTxt, name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
            val notificationManagerCompat: NotificationManagerCompat =
                NotificationManagerCompat.from(requireContext())
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            notificationManagerCompat.notify(0, builder.build())
        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showAddNotification(binding!!.habit!!.name)
            } else {
            }
        }

    private fun showEdit() {
        val habit: Habit = requireArguments().getParcelable(Habit.KEY)!!
        binding!!.habit = habit.clone()
        binding!!.txtHabitNameLayout.isEnabled = false
        binding!!.txtStartDateLayout.isEnabled = false
        binding!!.fab.setOnClickListener { _: View? ->
            viewModel!!.editHabit(
                binding!!.habit!!,
                (binding!!.CategorySpinner.selectedItem as Category).id!!
            )
        }
    }

    internal inner class HabitTextWatcher(textView: TextView) : TextWatcher {
        private val textView: TextView

        init {
            this.textView = textView
        }

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
        override fun afterTextChanged(editable: Editable) {
            when (textView.getId()) {
                R.id.txtHabitName -> binding!!.txtHabitNameLayout.error = null
                R.id.txtStartDatePicker -> binding!!.txtStartDateLayout.error = null
            }
        }
    }
}