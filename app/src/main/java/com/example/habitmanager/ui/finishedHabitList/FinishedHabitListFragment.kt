package com.example.habitmanager.ui.finishedHabitList

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.adapter.HabitAdapter
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.preferencies.ListPreferencies
import com.example.habitmanager.ui.habitList.HabitListViewModel
import com.example.habitmanager.utils.collectFlow
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentFinishedHabitListBinding
import com.example.habitmanagerkt.databinding.FragmentHabitListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class FinishedHabitListFragment : Fragment(), HabitAdapter.OnItemClickListener {
    private var _binding: FragmentFinishedHabitListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FinishedHabitListViewModel by viewModels()

    var adapter: HabitAdapter? = null
    val TAG = "habitList"
    val BOTTOM_SHEET_TAG = "bottomSheet"
    private var selectedHabit = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedHabitListBinding.inflate(inflater)

        val iconis = HabitManagerApplication.applicationContext().assets.open("pensamiento.png")
        val iconDrawable = Drawable.createFromStream(iconis, null)
        binding.img.setImageDrawable(iconDrawable)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRvHabit()
        initViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_orderByCategory -> {
                adapter!!.orderByCategory()
                return true
            }

            R.id.action_orderByName -> {
                adapter!!.orderByName()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.menu_order, menu)
    }

    private fun initRvHabit() {
        adapter = HabitAdapter(this)
        val linearLayoutManager = LinearLayoutManager(
            activity,
            RecyclerView.VERTICAL, false
        )
        binding.rvHabit.layoutManager = linearLayoutManager
        binding.rvHabit.adapter = adapter
    }

    private fun initViewModel() {
        viewModel.liveDataList.observe(viewLifecycleOwner) { liveDataList ->
            adapter!!.updateData(liveDataList)
            if(liveDataList.isNotEmpty()){
                sortList()
            }
        }
        viewModel.deletedHabit.observe(viewLifecycleOwner) { habit: Habit ->
            if (viewModel.isUndoEnabled) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.undoText) + habit.name,
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(R.string.undo) {
                        viewModel.undo()
                        adapter!!.undo(habit)
                    }
                    .show()
                viewModel.isUndoEnabled = false
            }
        }

        collectFlow(viewModel.emptyList){
            if(it){
                binding.img.visibility = View.VISIBLE
            }else{
                binding.img.visibility = View.GONE
            }
        }

        viewModel.getList()
    }

    private fun sortList() {
        if (ListPreferencies().getOrder() == "1" || ListPreferencies().getOrder()!!.isEmpty()) {
            adapter!!.orderByName()
        } else {
            adapter!!.orderByCategory()
        }
    }

    fun habitManagerFragment(bundle: Bundle?) {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_finishedHabitListFragment_to_habitManagerFragment, bundle)
    }

    fun deleteHabit() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.tittleDeleteHabit))
            .setMessage(
                getString(
                    R.string.messageDeleteDependency,
                    adapter!!.getItem(selectedHabit).name
                )
            )
            .setNegativeButton(
                android.R.string.cancel
            ) { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
            }.setPositiveButton(
                android.R.string.ok
            ) { dialogInterface: DialogInterface?, i: Int ->
                    showDeletedNotification(
                    adapter!!.getItem(
                        selectedHabit
                    ).name!!
                )
                viewModel!!.delete(adapter!!.getItem(selectedHabit))
                adapter!!.deleteHabit(selectedHabit)
                adapter!!.selectedPosition = -1
            }
            .show()
    }

    private fun showDeletedNotification(name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(
                requireContext(), HabitManagerApplication.channelId()
            )
                .setSmallIcon(R.drawable.splashicon)
                .setContentTitle(getString(R.string.delHabitNotTitle))
                .setContentText(getString(R.string.delHabitNotTxt, name))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
            val notificationManagerCompat: NotificationManagerCompat =
                NotificationManagerCompat.from(
                    requireContext()
                )
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
                showDeletedNotification(adapter!!.getItem(selectedHabit).name!!)
            } else {
            }
        }

    fun viewHabit() {
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_finishedHabitListFragment_to_habitViewFragment, setBundle())
    }

    fun setBundle(): Bundle {
        val bundle = Bundle()
        val habit: Habit = adapter!!.getItem(selectedHabit)
        bundle.putParcelable(Habit.KEY, habit)
        return bundle
    }

    override fun onItemClick(view: View?, position: Int) {
        if (!requireView().isSelected) {
            val modalBottomSheet = FinishedHabitListBottomSheet(this)
            modalBottomSheet.show(requireActivity().supportFragmentManager, BOTTOM_SHEET_TAG)

        }
        selectedHabit = position
    }

    fun recover() {
        val habit = adapter!!.getItem(selectedHabit)
        if (habit.hasFinished()){
            Snackbar.make(requireView(), R.string.endDateOnPast, Snackbar.LENGTH_SHORT).show()
        }else {
            habit.isFinished = false
            viewModel.edit(habit)
            NavHostFragment.findNavController(this)
                .navigate(R.id.habitListFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}