package com.example.imagevista.ui.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import com.example.imagevista.data.remote.dto.UnsplashImageDto
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.ui.component.ImageCard

@Composable
fun HomeScreen(
    image : List<UnsplashImage>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        image.forEach{image->
            ImageCard(image = image)
        }


    }
}