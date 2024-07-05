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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    var image: List<UnsplashImage> by mutableStateOf(emptyList())
        private set

    init {
        getImage()
    }

    private fun getImage() {
        viewModelScope.launch {
            val result = repository.getEditorialFeedImage()
            image = result;
        }
    }

}