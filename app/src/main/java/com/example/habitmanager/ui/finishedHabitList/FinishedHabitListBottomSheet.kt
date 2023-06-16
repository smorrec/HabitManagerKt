package com.example.habitmanager.ui.finishedHabitList

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.habitmanagerkt.databinding.ModalBottomSheetBinding
import com.example.habitmanagerkt.databinding.ModalBottomSheetFinishedBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FinishedHabitListBottomSheet(private val finishedHabitListFragment: FinishedHabitListFragment) :
    BottomSheetDialogFragment() {
    private var binding: ModalBottomSheetFinishedBinding? = null

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
        binding = ModalBottomSheetFinishedBinding.inflate(inflater, container, false)
        binding!!.dragHandle.setOnClickListener { dismiss() }
        binding!!.edit.setOnClickListener {
            finishedHabitListFragment.habitManagerFragment(finishedHabitListFragment.setBundle())
            dismiss()
        }
        binding!!.delete.setOnClickListener {
            finishedHabitListFragment.deleteHabit()
            dismiss()
        }
        binding!!.recover.setOnClickListener {
            finishedHabitListFragment.recover()
            dismiss()
        }
        binding!!.info.setOnClickListener {
            finishedHabitListFragment.viewHabit()
            dismiss()
        }
        //binding!!.statics.setOnClickListener { dismiss() }
        return binding!!.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        finishedHabitListFragment.adapter!!.selectedPosition = -1
        finishedHabitListFragment.adapter!!.notifyDataSetChanged()
    }
}