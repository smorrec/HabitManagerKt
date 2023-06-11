package com.example.habitmanager.ui.habit

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.habitmanagerkt.databinding.ModalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HabitListBottomSheet(private val habitListFragment: HabitListFragment) :
    BottomSheetDialogFragment() {
    private var binding: ModalBottomSheetBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: BottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet: ConstraintLayout = binding!!.standardBottomSheet
            BottomSheetBehavior.from<ConstraintLayout>(bottomSheet)
                .setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ModalBottomSheetBinding.inflate(inflater, container, false)
        binding!!.dragHandle.setOnClickListener { dismiss() }
        binding!!.edit.setOnClickListener {
            habitListFragment.habitManagerFragment(habitListFragment.setBundle())
            dismiss()
        }
        binding!!.delete.setOnClickListener {
            habitListFragment.deleteHabit()
            dismiss()
        }
        binding!!.complete.setOnClickListener { dismiss() }
        binding!!.info.setOnClickListener {
            habitListFragment.viewHabit()
            dismiss()
        }
        binding!!.statics.setOnClickListener { dismiss() }
        return binding!!.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        habitListFragment.adapter!!.selectedPosition = -1
        habitListFragment.adapter!!.notifyDataSetChanged()
    }
}