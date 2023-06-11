package com.example.habitmanager.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.habitmanager.data.event.dao.HabitEventDao
import com.example.habitmanager.data.habit.dao.HabitDao
import com.example.habitmanager.data.user.repository.UserRepository
import com.example.habitmanager.utils.collectFlow
import com.example.habitmanager.utils.hideKeyboard
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.java.KoinJavaComponent.get

class LoginFragment: Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()
    private val userRepository: UserRepository = get(UserRepository::class.java)
    private val habitDao: HabitDao = get(HabitDao::class.java)
    private val habitEventDao: HabitEventDao = get(HabitEventDao::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            hideKeyboard()
            viewModel.login()
        }

        collectFlow(viewModel.loginResult){
            when(it){
                LoginResult.SUCCESS -> {
                    userRepository.setFirebaseUser(Firebase.auth.currentUser!!)
                    NavHostFragment.findNavController(this)
                        .navigate(R.id.action_loginFragment_to_MainFragment)
                }

                LoginResult.FAILURE -> Snackbar.make(
                    requireView(),
                    "Error en el inicio de sesión",
                    Snackbar.LENGTH_LONG)
                    .show()

                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        requireActivity().findViewById<View>(R.id.bottom_navigation).visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}