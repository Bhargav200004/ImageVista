package com.example.imagevista

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.imagevista.ui.homeScreen.HomeScreen
import com.example.imagevista.ui.homeScreen.HomeScreenViewModel
import com.example.imagevista.ui.theme.ImageVistaTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageVistaTheme {
                val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
                val viewModel = viewModel<HomeScreenViewModel>()
                Scaffold (
                    modifier = Modifier.fillMaxSize()
                        .nestedScroll(scrollBehaviour.nestedScrollConnection)
                ){
                    HomeScreen(
                        scrollBehaviour = scrollBehaviour,
                        image = viewModel.image,
                        onImageClick = {},
                        onSearchClick = {},
                        onFABClick = {}
                    )
                }
            }
        }
    }
}
