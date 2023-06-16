package com.example.habitmanager.ui.signUp

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.habitmanager.HabitManagerApplication
import com.example.habitmanager.data.user.repository.UserRepository
import com.example.habitmanager.utils.collectFlow
import com.example.habitmanager.utils.hideKeyboard
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import org.koin.java.KoinJavaComponent.get

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()
    private val userRepository: UserRepository = get(UserRepository::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.viewModel = viewModel

        val iconis = HabitManagerApplication.applicationContext().assets.open("appicon.png")
        val iconDrawable = Drawable.createFromStream(iconis, null)
        binding.icon.setImageDrawable(iconDrawable)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            initObservers()
            hideKeyboard()
            viewModel.signUp()
        }

        //initObservers()
    }

    private fun initObservers() {

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

        collectFlow(viewModel.displayName){
            if(it.isBlank()){
                binding.displayNameLayout.isErrorEnabled = true
                binding.displayNameLayout.error = getString(com.example.habitmanagerkt.R.string.emailError)
                viewModel.displayNameErr = true
            }else{
                binding.displayNameLayout.error = null
                binding.displayNameLayout.isErrorEnabled = false
                viewModel.displayNameErr = false
            }
        }

        collectFlow(viewModel.password){
            if(it.isBlank()){
                binding.passLayout.isErrorEnabled = true
                binding.passLayout.error = getString(R.string.passwordError)
                viewModel.passwordErr = true
            }else{
                binding.passLayout.error = null
                binding.passLayout.isErrorEnabled = false
                viewModel.passwordErr = false
            }
        }

        collectFlow(viewModel.repeatedPass){
            if(it.isBlank()){
                binding.pass2Layout.isErrorEnabled = true
                binding.pass2Layout.error = getString(R.string.passwordError)
                viewModel.repeatedPassErr = true
            }else if(!binding.passLayout.isErrorEnabled && it != viewModel.password.value){
                binding.pass2Layout.isErrorEnabled = true
                binding.pass2Layout.error = getString(R.string.diferentPasswordError)
                viewModel.repeatedPassErr = true
            }else{
                binding.pass2Layout.error = null
                binding.pass2Layout.isErrorEnabled = false
                viewModel.repeatedPassErr = false
            }
        }

        collectFlow(viewModel.signUpResult){
            when(it){
                SignUpResult.SUCCESS -> {
                    userRepository.prepareUser(viewModel.displayName.value)
                    NavHostFragment.findNavController(this)
                        .navigate(R.id.action_signUpFragment_to_MainFragment)
                }
                SignUpResult.FAILURE -> Snackbar.make(
                    requireView(),
                    getString(R.string.signUpError),
                    Snackbar.LENGTH_LONG)
                    .show()

                else -> {}
            }
        }
    }
}