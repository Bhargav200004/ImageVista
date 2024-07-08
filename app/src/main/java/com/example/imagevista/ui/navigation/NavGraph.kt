package com.example.imagevista.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.imagevista.ui.favoritesScreen.FavoritesScreen
import com.example.imagevista.ui.fullImageScreen.FullImageScreen
import com.example.imagevista.ui.fullImageScreen.FullImageViewModel
import com.example.imagevista.ui.homeScreen.HomeScreen
import com.example.imagevista.ui.homeScreen.HomeScreenViewModel
import com.example.imagevista.ui.profileScreen.ProfileScreen
import com.example.imagevista.ui.searchScreen.SearchScreen
import com.example.imagevista.ui.searchScreen.SearchScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraphSetup(
    navController: NavHostController,
    scrollBehaviour: TopAppBarScrollBehavior,
    snackBarHostState: SnackbarHostState,
    searchQuery : String,
    onSearchQueryChange : (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {
        composable<Routes.HomeScreen> {
            val homeViewModel : HomeScreenViewModel = hiltViewModel()
            HomeScreen(
                snackBarHostState = snackBarHostState,
                snackBarEvent = homeViewModel.snackBarEvent,
                scrollBehaviour = scrollBehaviour,
                image = homeViewModel.image,
                onImageClick = { imageId ->
                    navController.navigate(Routes.FullImageScreen(imageId = imageId))
                },
                onSearchClick = { navController.navigate(Routes.SearchScreen) },
                onFABClick = { navController.navigate(Routes.FavoritesScreen) }
            )
        }

        composable<Routes.SearchScreen> {
            val searchScreenViewModel : SearchScreenViewModel = hiltViewModel()
            val searchedImage = searchScreenViewModel.searchImage.collectAsLazyPagingItems()
            SearchScreen(
                snackBarHostState = snackBarHostState,
                snackBarEvent =searchScreenViewModel.snackBarEvent,
                searchImages = searchedImage,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onSearch = {
                    searchScreenViewModel.searchImage(it)
                },
                onBackButtonClick = {navController.navigateUp()},
                onImageClick ={ imageId ->
                    navController.navigate(Routes.FullImageScreen(imageId = imageId))
                },
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
                snackBarHostState = snackBarHostState,
                snackBarEvent = fullImageViewModel.snackBarEvent,
                image = fullImageViewModel.image,
                onBackButtonClick = { navController.navigateUp() },
                onPhotographerNameClick = { profileLink->
                    navController.navigate(Routes.ProfileScreen(profileLink))
                },
                onImageDownloadClick = { url , title ->
                    fullImageViewModel.downloadImage(url , title)
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