package com.example.imagevista.ui.fullImageScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.imagevista.R
import com.example.imagevista.domain.model.UnsplashImage
import kotlin.math.truncate

@Composable
fun FullImageScreen(
    image : UnsplashImage?,
    onBackButtonClick : () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        var isLoading by remember { mutableStateOf(true) }
        var isError by remember { mutableStateOf(false) }
        val imageLoader = rememberAsyncImagePainter(
            model = image?.imageUrlRaw,
            onState = {imageState->
                isLoading = imageState is AsyncImagePainter.State.Loading
                isError = imageState is AsyncImagePainter.State.Error
            }
        )

        if (isLoading){
            CircularProgressIndicator()
        }
        Image(
            painter = if(isError.not()) imageLoader else painterResource(id = R.drawable.ic_error) ,
            contentDescription = null
        )
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp),
            onClick = onBackButtonClick
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back Button"
            )
        }
    }
}