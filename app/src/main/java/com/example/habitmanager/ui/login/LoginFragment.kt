package com.example.habitmanager.ui.login

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.user.model.User
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import java.io.InputStream

class LoginFragment: Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val ims = HabitManagerApplication.applicationContext().assets.open("deporte.png")
        val drawable = Drawable.createFromStream(ims, null)
        binding.loginIMG.setImageDrawable(drawable)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.user = User()
        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.user as User)
        }
        binding.btnSignUp.setOnClickListener {
            viewModel.signUp(binding.user as User)
        }


        viewModel.loginResult.observe(viewLifecycleOwner){
            when(it){
                LoginResult.SUCCESS -> NavHostFragment.
                    findNavController(this)
                    .navigate(R.id.action_loginFragment_to_MainFragment)

                LoginResult.FAILURE -> Snackbar.make(
                    requireView(),
                    "Error en el inicio de sesión",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}