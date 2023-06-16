package com.example.habitmanager.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.habitmanager.data.event.repository.HabitEventRepository
import com.example.habitmanager.data.habit.repository.HabitRepository
import com.example.habitmanager.data.user.repository.UserRepository
import com.example.habitmanager.utils.collectFlow
import com.example.habitmanager.worker.StandardPriorityNotificationWorker
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private var appBarConfiguration: AppBarConfiguration? = null
    private var binding: ActivityMainBinding? = null
    private val userRepository: UserRepository = get(UserRepository::class.java)
    private var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(R.id.MainFragment, R.id.habitListFragment, R.id.loginFragment, R.id.finishedHabitListFragment)
            .setOpenableLayout(binding!!.drawer).build()

        setupActionBarWithNavController(this, navController, appBarConfiguration!!)
        setUpBottomNavigationView()
        setUpNavigationView()
        setUpPickMedia()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            return
        }


    }

    private val requestPermissionLauncher =
        registerForActivityResult<String, Boolean>(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
            } else {
            }
        }

    private fun setUpPickMedia() {
        pickMedia =
            registerForActivityResult<PickVisualMediaRequest, Uri>(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                if (uri != null) {
                    val headerView = binding!!.navigationView.getHeaderView(0)
                    var image: Bitmap? = null
                    try {
                        image = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    (headerView.findViewById<View>(R.id.user_image) as ImageView).setImageBitmap(
                        image
                    )
                    userRepository.updatePicture(image!!)
                }
            }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    private fun setUpBottomNavigationView() {
        binding!!.bottomNavigation.setOnItemSelectedListener { item: MenuItem ->
            val navOptions: NavOptions = NavOptions.Builder().setPopUpTo(R.id.nav_graph, true).build()
            when (item.itemId) {
                R.id.homeMenu -> {
                    findNavController(
                        this,
                        R.id.nav_host_fragment_content_main
                    ).navigate(R.id.MainFragment, null, navOptions)
                    return@setOnItemSelectedListener true
                }

                R.id.listMenu -> {
                    findNavController(
                        this,
                        R.id.nav_host_fragment_content_main
                    ).navigate(R.id.habitListFragment, null, navOptions)
                    return@setOnItemSelectedListener true
                }

                R.id.completedMenu -> {
                    findNavController(
                        this,
                        R.id.nav_host_fragment_content_main
                    ).navigate(R.id.finishedHabitListFragment, null, navOptions)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun setUpNavigationView() {
        val headerView = binding!!.navigationView.getHeaderView(0)

        collectFlow(userRepository.userLogged){
            if(it){
                try {
                    initWorker()
                    updateHeader()
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }

        collectFlow(userRepository.userPrepared){
            if(it){
                updateHeader()
                userRepository.consumeFlowPrepared()
            }
        }

        (headerView.findViewById<View>(R.id.user_image) as ImageView).setOnClickListener {
            pickMedia!!.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding!!.navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            item.isCheckable = false
            when (item.itemId) {
                R.id.action_mainFragment -> {
                    findNavController(
                        this,
                        R.id.nav_host_fragment_content_main
                    ).navigate(R.id.MainFragment)
                    binding!!.bottomNavigation.selectedItemId = R.id.homeMenu
                }

                R.id.action_habitList -> {
                    findNavController(
                        this,
                        R.id.nav_host_fragment_content_main
                    ).navigate(R.id.habitListFragment)
                    binding!!.bottomNavigation.selectedItemId = R.id.listMenu
                }

                R.id.action_completed -> {
                    findNavController(
                        this,
                        R.id.nav_host_fragment_content_main
                    ).navigate(R.id.finishedHabitListFragment)
                    binding!!.bottomNavigation.selectedItemId = R.id.completedMenu
                }
                /*
                R.id.action_profile -> findNavController(
                    this,
                    R.id.nav_host_fragment_content_main
                ).navigate(R.id.MainFragment)
                */

                R.id.action_settings -> findNavController(
                    this,
                    R.id.nav_host_fragment_content_main
                ).navigate(R.id.settingsFragment)

                R.id.action_showAboutUs -> findNavController(
                    this,
                    R.id.nav_host_fragment_content_main
                ).navigate(R.id.aboutUsFragment)

                R.id.action_logOut -> logOut()
            }

            binding!!.drawer.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun initWorker(){
        val workManager = WorkManager.getInstance(applicationContext)
        val request = PeriodicWorkRequestBuilder<StandardPriorityNotificationWorker>(8, TimeUnit.HOURS)
            .build()

        workManager.enqueueUniquePeriodicWork("StandardPriorityNotificationWorker", ExistingPeriodicWorkPolicy.KEEP, request)
    }

    private fun updateHeader() {
        lifecycleScope.launch {
            val headerView = binding!!.navigationView.getHeaderView(0)
            (headerView.findViewById<View>(R.id.username) as TextView).text =
                userRepository.getDisplayName()
            (headerView.findViewById<View>(R.id.email) as TextView).text =
                userRepository.getEmail()
            getBitMap(userRepository.getProfilePicture())?.let{
                (headerView.findViewById<View>(R.id.user_image) as ImageView).setImageBitmap(it)
            }

        }
    }

    private fun getBitMap(byteArray: ByteArray?): Bitmap?{
        return if(byteArray != null) BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size) else null
    }

    private fun logOut(){
        val workManager = WorkManager.getInstance(applicationContext)

        workManager.cancelUniqueWork("StandardPriorityNotificationWorker")
        workManager.pruneWork()

        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_graph, true).build()

        findNavController(
            this,
            R.id.nav_host_fragment_content_main
        ).navigate(R.id.loginFragment, null, navOptions)

        userRepository.logOut()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment_content_main)
        return (navigateUp(navController, appBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    override fun onBackPressed() {
        if (binding!!.drawer.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}