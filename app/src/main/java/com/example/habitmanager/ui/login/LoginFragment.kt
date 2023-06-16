package com.example.habitmanager.ui.login

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.habitmanager.HabitManagerApplication
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.viewModel = viewModel

        val iconis = HabitManagerApplication.applicationContext().assets.open("appicon.png")
        val iconDrawable = Drawable.createFromStream(iconis, null)
        binding.icon.setImageDrawable(iconDrawable)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            initCollectors()
            hideKeyboard()
            viewModel.login()
        }

        binding.signUp.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_signUpFragment)
        }

    }

    private fun initCollectors(){
        collectFlow(viewModel.email){
            if(it.isBlank()){
                binding.emailLayout.isErrorEnabled = true
                binding.emailLayout.error = getString(com.example.habitmanagerkt.R.string.emailError)
                viewModel.emailErr = true
            }else{
                binding.emailLayout.error = null
                binding.emailLayout.isErrorEnabled = false
                viewModel.emailErr = false
            }
        }

        collectFlow(viewModel.password){
            if(it.isBlank()){
                binding.passLayout.isErrorEnabled = true
                binding.passLayout.error = getString(R.string.passwordError)
                viewModel.passwordErr = true
            }else if(it.length < 6){
                binding.passLayout.isErrorEnabled = true
                binding.passLayout.error = getString(R.string.passwordLengthError)
                viewModel.passwordErr = true
            }else{
                binding.passLayout.error = null
                binding.passLayout.isErrorEnabled = false
                viewModel.passwordErr = false
            }
        }

        collectFlow(viewModel.loginResult){
            when(it){
                LoginResult.SUCCESS -> {
                    NavHostFragment.findNavController(this)
                        .navigate(R.id.action_loginFragment_to_MainFragment)
                    viewModel.consumeLoginFlow()
                }

                LoginResult.FAILURE -> {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.loginError),
                        Snackbar.LENGTH_LONG)
                        .show()
                    viewModel.consumeLoginFlow()
                }

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