package com.example.imagevista

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.imagevista.domain.model.NetworkStatus
import com.example.imagevista.domain.repository.NetworkConnectivityObserver
import com.example.imagevista.ui.navigation.NavGraphSetup
import com.example.imagevista.ui.theme.ImageVistaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var connectivityObserver: NetworkConnectivityObserver

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {

            val status by connectivityObserver.networkStatus.collectAsState()

            ImageVistaTheme {
                val navController = rememberNavController()
                val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()

                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehaviour.nestedScrollConnection),
                    bottomBar = {
                        if (status == NetworkStatus.Connected){
                            Text(text = "Connected to Internet")
                        }
                        else{
                            Text(text = "No Internet Connection")
                        }
                    }
                ){
                    NavGraphSetup(
                        navController = navController,
                        scrollBehaviour = scrollBehaviour
                    )

                }
            }
        }
    }
}
