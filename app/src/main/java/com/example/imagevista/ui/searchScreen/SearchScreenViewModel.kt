package com.example.imagevista.ui.searchScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.domain.repository.ImageRepository
import com.example.imagevista.ui.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    private val _searchImages = MutableStateFlow<PagingData<UnsplashImage>>(PagingData.empty())
    val searchImage = _searchImages

    fun searchImage(query : String){
        viewModelScope.launch {
            try {
                repository.searchImages(query)
                    .cachedIn(viewModelScope)
                    .collect{ _searchImages.value = it }
            }
            catch (e : Exception){
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Something went wrong. ${e.message}"
                    )
                )
            }
        }
    }

    val favoriteImageIds : StateFlow<List<String>> = repository.getFavoriteImageIds()
        .catch {
            _snackBarEvent.send(
                SnackBarEvent(
                    message = "Something went wrong. ${it.message}"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()
        )

    fun toggleFavoriteStatus(image: UnsplashImage) {
        viewModelScope.launch {
            try {
                repository.toggleFavoriteStatus(image)
            }
            catch (e : Exception){
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Something went wrong. ${e.message}"
                    )
                )
            }
        }
    }

}