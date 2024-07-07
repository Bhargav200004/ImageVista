package com.example.imagevista.ui.homeScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagevista.data.mapper.toDomainModelList
import com.example.imagevista.data.remote.UnsplashApiService
import com.example.imagevista.data.remote.dto.UnsplashImageDto
import com.example.imagevista.di.AppModule
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.domain.repository.ImageRepository
import com.example.imagevista.ui.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    var image: List<UnsplashImage> by mutableStateOf(emptyList())
        private set


    init {
        getImage()
    }

    private fun getImage() {
        viewModelScope.launch {
            try {
                val result = repository.getEditorialFeedImage()
                image = result;
            }
            catch (e : UnknownHostException){
                _snackBarEvent.send(
                    element = SnackBarEvent(message = "No Internet Connection. Please check you network.")
                )
            }
            catch (e : Exception){
                _snackBarEvent.send(
                    element = SnackBarEvent(message = "SomeThing went wrong: ${e.message}")
                )
            }
        }
    }

}