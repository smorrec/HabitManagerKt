package com.example.habitmanager.ui.habit

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.adapter.HabitAdapter
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.model.Habit
import com.example.habitmanager.preferencies.ListPreferencies
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentHabitListBinding
import com.example.habitmanagerkt.databinding.ModalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.java.KoinJavaComponent.get

class HabitListFragment : Fragment(), HabitAdapter.OnItemClickListener {
    private var binding: FragmentHabitListBinding? = null
    private var viewModel: HabitListViewModel? = null
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
        binding = FragmentHabitListBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.fab.setOnClickListener { view1: View? -> habitManagerFragment(null) }
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
        binding!!.rvHabit.layoutManager = linearLayoutManager
        binding!!.rvHabit.adapter = adapter
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HabitListViewModel::class.java)
        viewModel!!.liveDataList.observe(viewLifecycleOwner) { liveDataList ->
            if(liveDataList.isNotEmpty()){
                adapter!!.updateData(liveDataList)
                sortList()
            }
        }
        viewModel!!.deletedHabit.observe(viewLifecycleOwner) { habit: Habit ->
            if (viewModel!!.isUndoEnabled) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.undoText) + habit.name,
                    Snackbar.LENGTH_SHORT
                )
                    .setAction(R.string.undo) {
                        viewModel!!.undo()
                        adapter!!.undo(habit)
                    }
                    .show()
                viewModel!!.isUndoEnabled = false
            }
        }
        viewModel!!.getList()
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
            .navigate(R.id.action_habitListFragment_to_habitManagerFragment, bundle)
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
            .navigate(R.id.action_habitListFragment_to_habitViewFragment, setBundle())
    }

    fun setBundle(): Bundle {
        val bundle = Bundle()
        val habit: Habit = adapter!!.getItem(selectedHabit)
        bundle.putParcelable(Habit.KEY, habit)
        return bundle
    }

    override fun onItemClick(view: View?, position: Int) {
        if (!requireView().isSelected) {
            val modalBottomSheet = HabitListBottomSheet(this)
            modalBottomSheet.show(requireActivity().supportFragmentManager, BOTTOM_SHEET_TAG)

        }
        selectedHabit = position
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
}