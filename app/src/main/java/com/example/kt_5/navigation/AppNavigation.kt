package com.example.kt_5.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kt_5.ui.detail.ConcertDetailScreen
import com.example.kt_5.ui.detail.ConcertDetailViewModel
import com.example.kt_5.ui.list.ConcertListScreen
import com.example.kt_5.ui.list.ConcertListViewModel
import com.example.kt_5.ui.profile.ProfileScreen
import com.example.kt_5.ui.profile.ProfileViewModel
import com.example.kt_5.ui.purchase.PurchaseScreen
import com.example.kt_5.ui.purchase.PurchaseViewModel

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            val viewModel: ConcertListViewModel = hiltViewModel()
            ConcertListScreen(
                viewModel = viewModel,
                onConcertClick = { concertId ->
                    navController.navigate("detail/$concertId")
                },
                onProfileClick = {
                    navController.navigate("profile")
                }
            )
        }

        composable(
            route = "detail/{concertId}",
            arguments = listOf(navArgument("concertId") { type = NavType.IntType })
        ) { backStackEntry ->
            val concertId = backStackEntry.arguments?.getInt("concertId") ?: 0
            val viewModel: ConcertDetailViewModel = hiltViewModel()
            ConcertDetailScreen(
                viewModel = viewModel,
                concertId = concertId,
                onBackClick = { navController.popBackStack() },
                onBuyClick = { id ->
                    navController.navigate("purchase/$id")
                }
            )
        }

        composable(
            route = "purchase/{concertId}",
            arguments = listOf(navArgument("concertId") { type = NavType.IntType })
        ) { backStackEntry ->
            val concertId = backStackEntry.arguments?.getInt("concertId") ?: 0
            val viewModel: PurchaseViewModel = hiltViewModel()
            PurchaseScreen(
                viewModel = viewModel,
                concertId = concertId,
                onBackClick = { navController.popBackStack() },
                onDoneClick = { navController.popBackStack("list", false) }
            )
        }

        composable("profile") {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onConcertClick = { concertId ->
                    navController.navigate("detail/$concertId")
                }
            )
        }
    }
}
