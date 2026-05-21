package com.example.kt_5.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kt_5.di.AppModule
import com.example.kt_5.ui.detail.ConcertDetailScreen
import com.example.kt_5.ui.detail.ConcertDetailViewModel
import com.example.kt_5.ui.list.ConcertListScreen
import com.example.kt_5.ui.list.ConcertListViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    appModule: AppModule
) {
    val listViewModel = remember { ConcertListViewModel(appModule.repository) }

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            ConcertListScreen(
                viewModel = listViewModel,
                onConcertClick = { concertId ->
                    navController.navigate("detail/$concertId")
                }
            )
        }

        composable(
            route = "detail/{concertId}",
            arguments = listOf(navArgument("concertId") { type = NavType.IntType })
        ) { backStackEntry ->
            val concertId = backStackEntry.arguments?.getInt("concertId") ?: 0
            val detailViewModel = remember { ConcertDetailViewModel(appModule.repository, concertId) }
            ConcertDetailScreen(
                viewModel = detailViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}