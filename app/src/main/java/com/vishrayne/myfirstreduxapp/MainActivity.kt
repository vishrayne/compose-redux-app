package com.vishrayne.myfirstreduxapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vishrayne.myfirstreduxapp.ui.products.ProductsScreen
import com.vishrayne.myfirstreduxapp.ui.theme.MyfirstreduxappTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState by mutableStateOf<MainActivityUiState>(MainActivityUiState.Loading)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    uiState = it
                }
            }
        }

        splashScreen.setKeepOnScreenCondition {
            uiState == MainActivityUiState.Loading
        }

        setContent {
            ReduxApp()
        }
    }

    @Composable
    fun ReduxApp() {
        val navigationState = rememberNavigationState()

        MyfirstreduxappTheme {
            Scaffold(
                scaffoldState = navigationState.scaffoldState,
                bottomBar = { ReduxAppBottomNavigation(navigationState = navigationState) }
            ) { innerPaddingModifier ->
                ReduxAppNavHost(
                    navigationState = navigationState,
                    modifier = Modifier.padding(innerPaddingModifier)
                )
            }
        }
    }

    @Composable
    fun ReduxAppBottomNavigation(
        navigationState: NavigationState,
    ) {
        BottomNavigation {
            val navBackStackEntry by navigationState.navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            navigationState.tabs.forEach { item ->
                BottomNavigationItem(
                    icon = { Icon(item.icon, contentDescription = null) },
//                label = { Text(stringResource(item.title)) },
                    label = { Text(item.title) },
                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                    onClick = {
                        navigationState.singleTopNavigate(item)
                    }
                )
            }
        }
    }
}