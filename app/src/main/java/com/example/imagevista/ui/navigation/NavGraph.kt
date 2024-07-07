package com.example.imagevista.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.imagevista.ui.favoritesScreen.FavoritesScreen
import com.example.imagevista.ui.fullImageScreen.FullImageScreen
import com.example.imagevista.ui.fullImageScreen.FullImageViewModel
import com.example.imagevista.ui.homeScreen.HomeScreen
import com.example.imagevista.ui.homeScreen.HomeScreenViewModel
import com.example.imagevista.ui.profileScreen.ProfileScreen
import com.example.imagevista.ui.searchScreen.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraphSetup(
    navController: NavHostController,
    scrollBehaviour: TopAppBarScrollBehavior
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {
        composable<Routes.HomeScreen> {
            val viewModel : HomeScreenViewModel = hiltViewModel()
            HomeScreen(
                scrollBehaviour = scrollBehaviour,
                image = viewModel.image,
                onImageClick = { imageId ->
                    navController.navigate(Routes.FullImageScreen(imageId = imageId))
                },
                onSearchClick = { navController.navigate(Routes.SearchScreen) },
                onFABClick = { navController.navigate(Routes.FavoritesScreen) }
            )
        }

        composable<Routes.SearchScreen> {
            SearchScreen(
                onBackButtonClick = { navController.navigateUp() }
            )
        }

        composable<Routes.FavoritesScreen> {
            FavoritesScreen(
                onBackButtonClick = { navController.navigateUp() }
            )
        }

        composable<Routes.FullImageScreen> {
            val fullImageViewModel : FullImageViewModel = hiltViewModel()
            FullImageScreen(
                image = fullImageViewModel.image,
                onBackButtonClick = { navController.navigateUp() },
                onPhotographerNameClick = { profileLink->
                    navController.navigate(Routes.ProfileScreen(profileLink))
                }
            )
        }

        composable<Routes.ProfileScreen> { backStackentry ->
            val profileLink = backStackentry.toRoute<Routes.ProfileScreen>().profileLink
            ProfileScreen(
                profileLink = profileLink,
                onBackButtonClick = { navController.navigateUp()}
            )
        }
    }
}