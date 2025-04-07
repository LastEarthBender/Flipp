package com.example.flipp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flipp.R
import com.example.flipp.databinding.ActivityMainBinding
import com.example.flipp.domain.usecases.ObserveNetworkStatusUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val binding by lazy<ActivityMainBinding>(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var observeNetworkStatusUseCase: ObserveNetworkStatusUseCase

    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpNavComponents()
        observeNetworkStatus()

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())
            ViewCompat.onApplyWindowInsets(view, windowInsets)
        }
    }

    private fun observeNetworkStatus() {
        lifecycleScope.launch {
            observeNetworkStatusUseCase().collectLatest { isOnline ->
                binding.offlineIndicator.isVisible = !isOnline
            }
        }
    }

    private fun setUpNavComponents() {
        binding.apply {
            setSupportActionBar(toolbar)

            val host = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
                    ?: return
            navController = host.navController

            appBarConfig = AppBarConfiguration(
                setOf(R.id.dashboardFragment, R.id.itemListFragment,R.id.reportsFragment), drawerLayout)

            setupActionBarWithNavController(
                navController = navController,
                configuration = appBarConfig
            )

            navView?.setupWithNavController(navController)
            bottomNav?.setupWithNavController(navController)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfig) || super.onSupportNavigateUp()
    }

}