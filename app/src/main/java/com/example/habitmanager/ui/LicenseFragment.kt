package com.example.habitmanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentLicenseBinding

class LicenseFragment : Fragment() {
    private var binding: FragmentLicenseBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLicenseBinding.inflate(inflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.licenseBtnMPA.setOnClickListener { showDescription(view) }
        binding!!.licenseBtnAbout.setOnClickListener { showDescription(view) }
        binding!!.licenseBtnStickers.setOnClickListener { showDescription(view) }
    }

    private fun hideDescription(view: View) {
        var content: CardView? = null
        val btn = view as ImageButton
        when (view.getId()) {
            R.id.licenseBtnMPA -> content = binding!!.contentMPA
            R.id.licenseBtnAbout -> content = binding!!.contentAbout
            R.id.licenseBtnStickers -> content = binding!!.contentStickers
        }
        content!!.visibility = View.GONE
        btn.setImageResource(R.drawable.ic_expand)
        btn.setOnClickListener { view: View -> showDescription(view) }
    }

    private fun showDescription(view: View) {
        var content: CardView? = null
        val btn = view as ImageButton
        when (view.getId()) {
            R.id.licenseBtnMPA -> content = binding!!.contentMPA
            R.id.licenseBtnAbout -> content = binding!!.contentAbout
            R.id.licenseBtnStickers -> content = binding!!.contentStickers
        }
        content!!.visibility = View.VISIBLE
        btn.setImageResource(R.drawable.ic_hide)
        btn.setOnClickListener { view: View -> hideDescription(view) }
    }
}