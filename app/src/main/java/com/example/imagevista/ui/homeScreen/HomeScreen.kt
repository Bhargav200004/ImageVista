package com.example.imagevista.ui.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.imagevista.R
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.ui.component.ImageVerticalGrid
import com.example.imagevista.ui.component.ImageVistaTopApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    image: List<UnsplashImage>,
    onImageClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    scrollBehaviour: TopAppBarScrollBehavior,
    onFABClick : () -> Unit
) {
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
                images = image,
                onImageClickable = onImageClick
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
    }

}