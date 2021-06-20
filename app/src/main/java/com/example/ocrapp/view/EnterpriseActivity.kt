package com.example.ocrapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.ocrapp.R
import com.example.ocrapp.databinding.ActivityEnterpriseBinding

class EnterpriseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnterpriseBinding
    private lateinit var navController: NavController
    private lateinit var actualFragment: NavDestination

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterpriseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_enterprise) as NavHostFragment

        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)

        navigationListener()
    }

    private fun navigationListener() {

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            binding.apply {

                when (destination.id) {

                    R.id.cameraEnterprise -> topBar.title = "Cámara"

                }
            }


        }

    }
}