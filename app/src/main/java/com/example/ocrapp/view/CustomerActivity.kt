package com.example.ocrapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.ocrapp.R
import com.example.ocrapp.databinding.ActivityCustomerBinding

class CustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerBinding
    private lateinit var navController: NavController
    private lateinit var actualFragment: NavDestination

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_customer) as NavHostFragment

        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        navigationListener()

    }

    private fun navigationListener() {


        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            binding.apply {
                when (destination.id) {
                    R.id.cameraFragment -> topBar.title = getString(R.string.cameraMenu)
                    R.id.metersFragment -> topBar.title = getString(R.string.metersMenu)
                    R.id.homeFragment -> topBar.title = getString(R.string.homeMenu)
                }
            }


        }


    }


}