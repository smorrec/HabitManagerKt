package com.example.habitmanager.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.habitmanager.worker.DailyResetWorker
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.FragmentSplashBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.concurrent.TimeUnit

class SplashFragment : Fragment() {
    private val WAIT_TIME = 2000
    private var binding: FragmentSplashBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onStart() {
        super.onStart()
        if(Firebase.auth.currentUser != null){
            initWorker()
            Handler().postDelayed({
                NavHostFragment.findNavController(this@SplashFragment)
                    .navigate(R.id.action_SplashFragment_to_MainFragment)
            }, WAIT_TIME.toLong())
        }else{
            Handler().postDelayed({
                NavHostFragment.findNavController(this@SplashFragment)
                    .navigate(R.id.action_SplashFragment_to_firstScreenFragment)
            }, WAIT_TIME.toLong())
        }

    }

    private fun initWorker(){
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, 23)
        c.set(Calendar.MINUTE, 59)
        c.set(Calendar.SECOND, 59)

        val delay = c.timeInMillis - System.currentTimeMillis()

        val workManager = WorkManager.getInstance(requireContext())
        val request = PeriodicWorkRequestBuilder<DailyResetWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork("DailyResetWorker", ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        requireActivity().findViewById<View>(R.id.bottom_navigation).visibility = View.GONE
    }
}