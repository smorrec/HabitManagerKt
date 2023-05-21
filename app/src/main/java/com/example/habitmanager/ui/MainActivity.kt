package com.example.habitmanager.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.habitmanager.data.user.model.User
import com.example.habitmanager.data.user.repository.UserRepository
import com.example.habitmanager.preferencies.UserPrefManager
import com.example.habitmanagerkt.R
import com.example.habitmanagerkt.databinding.ActivityMainBinding
import org.koin.java.KoinJavaComponent.get
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var appBarConfiguration: AppBarConfiguration? = null
    private var binding: ActivityMainBinding? = null
    private var userLogged: User? = null
    private var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private var userPrefManager: UserPrefManager? = null
    //private val userRepository: UserRepository = get(UserRepository::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        val navController = findNavController(this, R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration.Builder(R.id.MainFragment, R.id.habitListFragment)
            .setOpenableLayout(binding!!.drawer).build()
        setupActionBarWithNavController(this, navController, appBarConfiguration!!)
        setUpBottomNavigationView()
        //placeHolderUserPref()
        //setUpPickMedia()
        setUpNavigationView()
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
    /*
    private fun setUpPickMedia() {
        pickMedia =
            registerForActivityResult<PickVisualMediaRequest, Uri>(PickVisualMedia()) { uri: Uri? ->
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
                }
            }
    }
    */

    private fun placeHolderUserPref() {
        userPrefManager = UserPrefManager()
        if (!userPrefManager!!.isUserLogged()) {
            //userPrefManager!!.login(userRepository.list!!.get(0).email)
        }
        //userLogged = userRepository.list!!.get(0)
        userLogged!!.email = userPrefManager!!.getUserEmail()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return super.onOptionsItemSelected(item)
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

                R.id.completedMenu -> return@setOnItemSelectedListener true
            }
            false
        }
    }

    private fun setUpNavigationView() {
        val headerView = binding!!.navigationView.getHeaderView(0)
        //(headerView.findViewById<View>(R.id.username) as TextView).text = userLogged!!.name
        //(headerView.findViewById<View>(R.id.email) as TextView).text = userLogged!!.email
        //(headerView.findViewById<View>(R.id.user_image) as ImageView).setImageResource(
            //userLogged!!.profilePicture
       //)
        //headerView.findViewById<View>(R.id.user_image)
            //.setOnClickListener { view: View? -> pickMedia() }
        binding!!.navigationView.setNavigationItemSelectedListener { item: MenuItem ->
            item.isCheckable = true
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
                    ).navigate(R.id.MainFragment)
                    binding!!.bottomNavigation.selectedItemId = R.id.completedMenu
                }

                R.id.action_profile -> findNavController(
                    this,
                    R.id.nav_host_fragment_content_main
                ).navigate(R.id.MainFragment)

                R.id.action_settings -> findNavController(
                    this,
                    R.id.nav_host_fragment_content_main
                ).navigate(R.id.settingsFragment)

                R.id.action_showAboutUs -> findNavController(
                    this,
                    R.id.nav_host_fragment_content_main
                ).navigate(R.id.aboutUsFragment)
            }
            binding!!.drawer.closeDrawer(GravityCompat.START)
            true
        }
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
/*
    fun pickMedia() {
        pickMedia!!.launch(
            Builder()
                .setMediaType(ImageOnly).build()
        )
    }
    */

}