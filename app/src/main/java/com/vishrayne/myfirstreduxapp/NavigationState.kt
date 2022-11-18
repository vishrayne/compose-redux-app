package com.vishrayne.myfirstreduxapp

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

sealed class Destination(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    object ProductsDestination : Destination("Home", Icons.Filled.Home, "products")
    object CartDestination : Destination("Cart", Icons.Filled.ShoppingCart, "cart")
    object ProfileDestination : Destination("Profile", Icons.Filled.Person, "profile")
}

class NavigationState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    val initialDestination: Destination,
    val resources: Resources,
) {
    val tabs = listOf(
        Destination.ProductsDestination,
        Destination.CartDestination,
        Destination.ProfileDestination
    )

    //TODO: Add navigation methods
    fun singleTopNavigate(destination: Destination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    private fun navigateTo(
        destination: Destination,
        from: NavBackStackEntry,
    ) {
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            // Make sure to remove any existing snackbar before presenting a new screen!
            scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()

            navController.navigate(destination.route)
        }
    }
}

@Composable
fun rememberNavigationState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    initialDestination: Destination = Destination.ProductsDestination,
    resources: Resources = resources(),
) = remember(
    scaffoldState,
    navController,
    initialDestination,
    resources,
) {
    NavigationState(
        scaffoldState,
        navController,
        initialDestination,
        resources
    )
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

/**
 * A composable function that returns the [Resources]. It will be recomposed when `Configuration`
 * gets updated.
 */
@Composable
@ReadOnlyComposable
private fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
