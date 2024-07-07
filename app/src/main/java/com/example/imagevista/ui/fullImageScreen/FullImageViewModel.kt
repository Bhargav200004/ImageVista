package com.example.imagevista.ui.fullImageScreen

import android.icu.text.CaseMap.Title
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.domain.repository.Downloader
import com.example.imagevista.domain.repository.ImageRepository
import com.example.imagevista.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullImageViewModel @Inject constructor(
    private val repository : ImageRepository,
    private val downloader: Downloader,
    savedStateHandle: SavedStateHandle
): ViewModel()   {

    private val imageId = savedStateHandle.toRoute<Routes.FullImageScreen>().imageId

    var image : UnsplashImage? by mutableStateOf(null)
        private set

    init {
        getImage()
    }

    private fun getImage(){
        viewModelScope.launch {
            try {
                val result = repository.getImage(imageId = imageId)
                image = result
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

    fun downloadImage(uri : String , title: String?){
        viewModelScope.launch {
            try {
                downloader.downloadFile(uri , title)
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }
    }

}