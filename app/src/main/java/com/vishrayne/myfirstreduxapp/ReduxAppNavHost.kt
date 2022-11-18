package com.vishrayne.myfirstreduxapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vishrayne.myfirstreduxapp.ui.products.ProductsScreen

@Composable
fun ReduxAppNavHost(
    navigationState: NavigationState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navigationState.navController,
        startDestination = navigationState.initialDestination.route,
        modifier = modifier
    ) {
        composable(Destination.ProductsDestination.route) {
            ProductsScreen(productsViewModel = hiltViewModel())
        }
        composable(Destination.CartDestination.route) {
            PlaceHolderScreen("Cart")
        }
        composable(Destination.ProfileDestination.route) {
            PlaceHolderScreen("Profile")
        }
    }
}

@Composable
fun PlaceHolderScreen(label: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "$label Screen",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}