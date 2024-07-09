package com.example.imagevista.ui.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.rememberTextMeasurer
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
fun HomeScreen(
    snackBarHostState : SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    scrollBehaviour: TopAppBarScrollBehavior,
    images: LazyPagingItems<UnsplashImage>,
    favoriteImageIds: List<String>,
    onImageClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onToggleFavoriteStatus: (UnsplashImage) -> Unit,
    onFABClick : () -> Unit
) {

    var showImagePreview by remember { mutableStateOf(false) }
    var activeImage by remember{ mutableStateOf<UnsplashImage?>(null)}


    LaunchedEffect(key1 = true) {
        snackBarEvent.collect{event->
            snackBarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )

        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageVistaTopApp(
                scrollBehaviour = scrollBehaviour,
                onSearchClick = onSearchClick
            )
            ImageVerticalGrid(
                images = images,
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
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            onClick = onFABClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_save),
                contentDescription = "Save button" ,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        ZoomedImageCard(
            modifier = Modifier
                .padding(20.dp),
            isVisibility = showImagePreview,
            image =activeImage
        )
    }

}