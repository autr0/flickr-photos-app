package com.example.pix.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.pix.R
import com.example.pix.ui.MainScreenViewModel
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    vm: MainScreenViewModel = hiltViewModel(),
    navigateToDetail: (String, String) -> Unit
) {
    val pictures = vm.pictures.collectAsLazyPagingItems()
    val searchQuery by vm.query.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = pictures.loadState) {
        if (pictures.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (pictures.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = searchQuery,
                        onValueChange = { vm.updateQuery(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = stringResource(id = R.string.search), color = Color.LightGray)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedLabelColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            focusedIndicatorColor = Color.Gray,
                            unfocusedIndicatorColor = Color.DarkGray,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            unfocusedTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            cursorColor = Color.DarkGray
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                            }
                        )
                    )
                },
                actions = {
                    AnimatedVisibility(visible = searchQuery.isNotEmpty()) {
                        IconButton(onClick = { vm.clearQuery() }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                contentDescription = "clear textField"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .fillMaxSize()
        ) {
            if (pictures.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2)
                ) {
                    items(
                        count = pictures.itemCount,
                        key = pictures.itemKey(),
                        contentType = pictures.itemContentType { "pictureViews" }
                    ) { index ->
                        pictures[index]?.let { pictureView ->
                            PictureCard(
                                url = pictureView.url,
                                title = pictureView.title,
                                navigateToDetail = navigateToDetail
                            )
                        }
                    }
                    item {
                        if (pictures.loadState.append is LoadState.Loading) {
                            Box(
                                modifier = Modifier.size(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(100.dp).padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun PictureCard(
    url: String,
    title: String,
    navigateToDetail: (String, String) -> Unit
) {
    Card(
        modifier = Modifier
            .size(200.dp)
            .padding(2.dp)
            .clickable { navigateToDetail(url, title) },
        shape = RoundedCornerShape(15.dp)
    ) {
        AsyncImage(
            model = url,
            placeholder = painterResource(R.drawable.image_placeholder),
            error = painterResource(R.drawable.error_image),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(2.dp)
                .fillMaxSize()
        )
    }
}