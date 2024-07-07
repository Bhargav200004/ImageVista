package com.example.imagevista.ui.fullImageScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.imagevista.R
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.ui.component.FullImageViewTopBar
import com.example.imagevista.ui.component.ImageVistaLoadingBar
import com.example.imagevista.ui.util.rememberWindowInsetsController
import com.example.imagevista.ui.util.toggleStatusBars
import kotlinx.coroutines.launch
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullImageScreen(
    image: UnsplashImage?,
    onBackButtonClick: () -> Unit,
    onPhotographerImgClick: (String) -> Unit,
) {

    val scope = rememberCoroutineScope()
    var showBars by remember { mutableStateOf(false) }
    val windowInsetsController = rememberWindowInsetsController()

    LaunchedEffect(key1 = Unit) {
      windowInsetsController.toggleStatusBars(show = showBars)
    }

    BackHandler(enabled = !showBars) {
        windowInsetsController.toggleStatusBars(show = true)
        onBackButtonClick()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            var scale by remember { mutableFloatStateOf(1f) }
            var offset by remember { mutableStateOf(Offset.Zero) }

            val isImageZoomed : Boolean by remember { derivedStateOf{scale != 1f} }

            val transformState = rememberTransformableState { zoomChange, offsetChange, _ ->
                scale = max(scale * zoomChange, 1f)
                val maxX = (constraints.maxWidth * (scale - 1 )) / 2
                val maxY = (constraints.maxHeight * (scale - 1)) / 2
                    offset = Offset(
                        x = (offset.x + offsetChange.x).coerceIn(-maxX , maxX),
                        y = (offset.y + offsetChange.y).coerceIn(-maxY , maxY)
                    )
            }

            var isLoading by remember { mutableStateOf(true) }
            var isError by remember { mutableStateOf(false) }
            val imageLoader = rememberAsyncImagePainter(
                model = image?.imageUrlRaw,
                onState = { imageState ->
                    isLoading = imageState is AsyncImagePainter.State.Loading
                    isError = imageState is AsyncImagePainter.State.Error
                }
            )

            if (isLoading) {
                ImageVistaLoadingBar(
                    size = 80.dp
                )
            }
            Image(
                painter = if (!isError.not()) painterResource(id = R.drawable.ic_error) else imageLoader,
                contentDescription = null,
                modifier = Modifier
                    .transformable(transformState)
                    .combinedClickable(
                        onDoubleClick = {
                            if (isImageZoomed) {
                                scale = 1f
                                offset = Offset.Zero
                            } else {
                                scope.launch {
                                    transformState.animateZoomBy(zoomFactor = 3f)
                                }

                            }
                        },
                        onClick = {
                            showBars = !showBars
                            windowInsetsController.toggleStatusBars(show = showBars)
                        },
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    )
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offset.x
                        translationY = offset.y
                    }
            )
        }

        FullImageViewTopBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 40.dp),
            image = image,
            onBackButtonClick = onBackButtonClick,
            onPhotographerImgClick = onPhotographerImgClick,
            onDownloadImgClick = {},
            isVisible = showBars
        )
    }
}