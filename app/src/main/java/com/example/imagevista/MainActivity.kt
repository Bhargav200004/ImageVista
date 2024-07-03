package com.example.imagevista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imagevista.ui.homeScreen.HomeScreen
import com.example.imagevista.ui.homeScreen.HomeScreenViewModel
import com.example.imagevista.ui.theme.ImageVistaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageVistaTheme {
                val viewModel = viewModel<HomeScreenViewModel>()
                HomeScreen(
                    image = viewModel.image,
                    onImageClick = {}
                )
            }
        }
    }
}
