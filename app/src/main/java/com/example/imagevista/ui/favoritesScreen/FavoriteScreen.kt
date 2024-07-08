package com.example.imagevista.ui.favoritesScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.imagevista.R
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.ui.component.ImageVerticalGrid
import com.example.imagevista.ui.component.ImageVistaTopApp
import com.example.imagevista.ui.component.ZoomedImageCard
import com.example.imagevista.ui.util.SnackBarEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    snackBarHostState: SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    favoriteImage: LazyPagingItems<UnsplashImage>,
    favoriteImageIds: List<String>,
    scrollBehaviour: TopAppBarScrollBehavior,
    onImageClick: (String) -> Unit,
    onBackButtonClick: () -> Unit,
    onToggleFavoriteStatus: (UnsplashImage) -> Unit,
    onSearchClick: () -> Unit,
) {


    var showImagePreview by remember { mutableStateOf(false) }
    var activeImage by remember { mutableStateOf<UnsplashImage?>(null) }


    LaunchedEffect(key1 = true) {
        snackBarEvent.collect { event ->
            snackBarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )

        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageVistaTopApp(
                title = "Favorite Image",
                scrollBehaviour = scrollBehaviour,
                onSearchClick = onSearchClick,
                navigationIcon = {
                    IconButton(onClick = { onBackButtonClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                }
            )

            ImageVerticalGrid(
                images = favoriteImage,
                onImageClickable = onImageClick,
                favoriteImageIds = favoriteImageIds,
                onImageDragStart = { image ->
                    activeImage = image
                    showImagePreview = true
                },
                onImageDragEnd = {
                    showImagePreview = false
                },
                onToggleFavoriteStatus = onToggleFavoriteStatus
            )
        }
        ZoomedImageCard(
            modifier = Modifier
                .padding(20.dp),
            isVisibility = showImagePreview,
            image = activeImage
        )
        if(favoriteImage.itemCount == 0 ){
            EmptyState(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            )
        }
    }
}


@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.img_empty_bookmarks),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "No Saved Image",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Images you save will be stored here",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )

    }
}