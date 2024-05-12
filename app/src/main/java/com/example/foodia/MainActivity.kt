package com.example.foodia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodia.ui.navigation.FoodiaNavigationHost
import com.example.foodia.ui.navigation.Tabs
import com.example.foodia.ui.navigation.navigateToBottomBarRoute
import com.example.foodia.ui.onboarding.OnBoardingViewModel
import com.example.foodia.ui.theme.FoodiaTheme

class MainActivity : ComponentActivity() {
    private val onBoardingViewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            onBoardingViewModel.isLoading.value
        }
        setContent {
            FoodiaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FoodiaApp(onBoardingViewModel)
                }
            }
        }
    }

    @Composable
    fun FoodiaApp(onBoardingViewModel: OnBoardingViewModel) {

        val navController = rememberNavController()
        val backStackState by navController.currentBackStackEntryAsState()
        val currentDestination = backStackState?.destination?.route
        val showBottomBar =
            currentDestination == Tabs.Feed.route || currentDestination == Tabs.Cart.route
        val (tabIndex, setTabIndex) = remember {
            mutableIntStateOf(0)
        }
        val startDestination by onBoardingViewModel.startDestination.collectAsState()

        LaunchedEffect(key1 = currentDestination) {
            when (currentDestination) {
                Tabs.Feed.route -> setTabIndex(0)
                Tabs.Cart.route -> setTabIndex(1)
            }
        }
        val tabs = Tabs.entries
        Scaffold(
            bottomBar = {
                AnimatedVisibility(visible = showBottomBar) {
                    TabRow(selectedTabIndex = tabIndex) {
                        tabs.forEachIndexed { index, route ->
                            Tab(
                                selected = index == tabIndex,
                                onClick = {
                                    setTabIndex(index)
                                    navController.navigateToBottomBarRoute(route.route)
                                },
                                icon = {
                                    Icon(
                                        imageVector = route.icon,
                                        contentDescription = route.title,
                                    )
                                },
                                text = {
                                    Text(text = route.title)
                                }
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            FoodiaNavigationHost(
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues),
                navController = navController
            )
        }
    }
}
