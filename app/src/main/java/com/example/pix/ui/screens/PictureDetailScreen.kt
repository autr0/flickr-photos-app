package com.example.pix.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.pix.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictureDetailScreen(
    url: String,
    title: String,
    navigateBack: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    var initialOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    val slowMovement = 0.5f

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (title.isNotBlank()) {
                        Text(
                            text = title,
                            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "arrow_back",
                            tint = if (isSystemInDarkTheme()) Color.White else Color.Black
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            val newScale = scale * zoom
                            scale = newScale.coerceIn(1f, 4f)

                            val centerX = size.width / 2f
                            val centerY = size.height / 2f
                            val offsetXChange = (centerX - offsetX) * (newScale / scale - 1)
                            val offsetYChange = (centerY - offsetY) * (newScale / scale - 1)

                            val maxOffsetX = (size.width / 2) * (scale - 1)
                            val minOffsetX = -maxOffsetX
                            val maxOffsetY = (size.height / 2) * (scale - 1)
                            val minOffsetY = -maxOffsetY


                            if (scale * zoom <= 4f) {
                                offsetX =
                                    (offsetX + pan.x * scale * slowMovement + offsetXChange)
                                        .coerceIn(minOffsetX, maxOffsetX)
                                offsetY =
                                    (offsetY + pan.y * scale * slowMovement + offsetYChange)
                                        .coerceIn(minOffsetY, maxOffsetY)
                            }

                            if (pan != Offset(0f, 0f) && initialOffset == Offset(0f, 0f)) {
                                initialOffset = Offset(offsetX, offsetY)
                            }
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (scale != 1f) {
                                    scale = 1f
                                    offsetX = initialOffset.x
                                    offsetY = initialOffset.y
                                } else {
                                    scale = 2f
                                }
                            }
                        )
                    }
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = offsetX
                        translationY = offsetY
                    },
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = url,
                    contentDescription = title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth(),
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(128.dp)
                                .size(64.dp)
                                .align(Alignment.Center)
                        )
                    },
                    error = {
                        Image(painterResource(R.drawable.error_image), contentDescription = "error_image")
                    }
                )
            }
        }
    }
}