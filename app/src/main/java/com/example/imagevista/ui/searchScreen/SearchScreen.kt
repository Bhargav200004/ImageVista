package com.example.imagevista.ui.searchScreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.imagevista.data.util.Constant
import com.example.imagevista.domain.model.UnsplashImage
import com.example.imagevista.ui.component.ImageVerticalGrid
import com.example.imagevista.ui.component.ZoomedImageCard
import com.example.imagevista.ui.util.SnackBarEvent
import com.example.imagevista.ui.util.searchKeywords
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    snackBarHostState : SnackbarHostState,
    snackBarEvent: Flow<SnackBarEvent>,
    searchImages : LazyPagingItems<UnsplashImage>,
    searchQuery : String,
    onSearchQueryChange : (String) -> Unit,
    onImageClick: (String) -> Unit,
    onSearch : (String) -> Unit,
    onBackButtonClick : () -> Unit
) {

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isSuggestionChipVisible by remember { mutableStateOf(false)}

    var showImagePreview by remember { mutableStateOf(false) }
    var activeImage by remember{ mutableStateOf<UnsplashImage?>(null) }




    Log.d(Constant.IV_LOG_TAG, "SearchIMagesCount: ${searchImages.itemCount}" )



    LaunchedEffect(key1 = true) {
        snackBarEvent.collect{event->
            snackBarHostState.showSnackbar(
                message = event.message,
                duration = event.duration
            )

        }
    }

    LaunchedEffect(key1 = Unit) {
        delay(500)
        focusRequester.requestFocus()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .onFocusChanged { isSuggestionChipVisible = it.isFocused },
                query = searchQuery,
                onQueryChange = {onSearchQueryChange(it)},
                onSearch = {
                    onSearch(searchQuery)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                placeholder = { Text(text = "Search....")},
                leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        if (searchQuery.isNotEmpty()) onSearchQueryChange("")  else onBackButtonClick()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Close"
                        )
                    }
                },
                active =false,
                onActiveChange ={},
                content =  {}
            )
            
            AnimatedVisibility(visible = isSuggestionChipVisible) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    items(searchKeywords){ keyword->
                        SuggestionChip(
                            onClick = {
                                onSearch(searchQuery)
                                onSearchQueryChange(keyword)
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                            label = { Text(text = keyword) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }
            
            ImageVerticalGrid(
                images = searchImages,
                onImageClickable = onImageClick,
                onImageDragStart = {image ->
                    activeImage = image
                    showImagePreview = true
                },
                onImageDragEnd ={
                    showImagePreview = false
                }
            )
        }
        ZoomedImageCard(
            modifier = Modifier
                .padding(20.dp),
            isVisibility = showImagePreview,
            image =activeImage
        )
    }
}