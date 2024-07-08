package com.example.imagevista.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.imagevista.ui.favoritesScreen.FavoriteScreenViewModel
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
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {
        composable<Routes.HomeScreen> {
            val homeViewModel: HomeScreenViewModel = hiltViewModel()
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
            val searchScreenViewModel: SearchScreenViewModel = hiltViewModel()
            val searchedImage = searchScreenViewModel.searchImage.collectAsLazyPagingItems()
            val favoriteImageIds by searchScreenViewModel.favoriteImageIds.collectAsStateWithLifecycle()
            SearchScreen(
                snackBarHostState = snackBarHostState,
                snackBarEvent = searchScreenViewModel.snackBarEvent,
                searchImages = searchedImage,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                favoriteImageIds = favoriteImageIds,
                onBackButtonClick = { navController.navigateUp() },
                onImageClick = { imageId ->
                    navController.navigate(Routes.FullImageScreen(imageId = imageId))
                },
                onSearch = {
                    searchScreenViewModel.searchImage(it)
                },
                onToggleFavoriteStatus = { searchScreenViewModel.toggleFavoriteStatus(it) }
            )
        }

        composable<Routes.FavoritesScreen> {
            val favoritesViewModel: FavoriteScreenViewModel = hiltViewModel()
            val favoriteImage = favoritesViewModel.favoriteImage.collectAsLazyPagingItems()
            val favoriteImageIds by favoritesViewModel.favoriteImageIds.collectAsStateWithLifecycle()
            FavoritesScreen(
                snackBarHostState = snackBarHostState,
                snackBarEvent = favoritesViewModel.snackBarEvent,
                favoriteImage = favoriteImage,
                scrollBehaviour = scrollBehaviour,
                onSearchClick = { navController.navigate(Routes.SearchScreen) },
                favoriteImageIds = favoriteImageIds,
                onImageClick = { imageId ->
                    navController.navigate(Routes.FullImageScreen(imageId = imageId))
                },
                onBackButtonClick = { navController.navigateUp() },
                onToggleFavoriteStatus ={ favoritesViewModel.toggleFavoriteStatus(it)}
            )
        }

        composable<Routes.FullImageScreen> {
            val fullImageViewModel: FullImageViewModel = hiltViewModel()
            FullImageScreen(
                snackBarHostState = snackBarHostState,
                snackBarEvent = fullImageViewModel.snackBarEvent,
                image = fullImageViewModel.image,
                onBackButtonClick = { navController.navigateUp() },
                onPhotographerNameClick = { profileLink ->
                    navController.navigate(Routes.ProfileScreen(profileLink))
                },
                onImageDownloadClick = { url, title ->
                    fullImageViewModel.downloadImage(url, title)
                }
            )
        }

        composable<Routes.ProfileScreen> { backStackentry ->
            val profileLink = backStackentry.toRoute<Routes.ProfileScreen>().profileLink
            ProfileScreen(
                profileLink = profileLink,
                onBackButtonClick = { navController.navigateUp() }
            )
        }
    }
}