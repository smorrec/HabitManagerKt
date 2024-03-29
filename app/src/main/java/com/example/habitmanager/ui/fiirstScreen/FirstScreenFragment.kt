package com.example.habitmanager.ui.fiirstScreen

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentFirstScreenBinding
import com.example.habitmanagerkt.databinding.FragmentLoginBinding

class FirstScreenFragment : Fragment() {
    private var _binding: FragmentFirstScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFirstScreenBinding.inflate(inflater, container, false)
        val ims = HabitManagerApplication.applicationContext().assets.open("deporte.png")
        val drawable = Drawable.createFromStream(ims, null)
        binding.IMG.setImageDrawable(drawable)

        val iconis = HabitManagerApplication.applicationContext().assets.open("appicon.png")
        val iconDrawable = Drawable.createFromStream(iconis, null)
        binding.icon.setImageDrawable(iconDrawable)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button1.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_firstScreenFragment_to_loginFragment)
        }
        binding.button2.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_firstScreenFragment_to_signUpFragment)
        }
    }

}