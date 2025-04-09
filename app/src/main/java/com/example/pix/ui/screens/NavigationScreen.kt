package com.example.pix.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

@Composable
fun NavigationScreen() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Main
    ) {
        composable<Main> {
            MainScreen(
                navigateToDetail = { url, title ->
                    navController.navigate(
                        PictureDetail(
                            url = url.replace(".jpg", "_b.jpg"),
                            title = title
                        )
                    )
                }
            )
        }

        composable<PictureDetail> {
            PictureDetailScreen(
                url = it.toRoute<PictureDetail>().url,
                title = it.toRoute<PictureDetail>().title,
                navigateBack = { navController.navigateUp() }
            )
        }
    }

}