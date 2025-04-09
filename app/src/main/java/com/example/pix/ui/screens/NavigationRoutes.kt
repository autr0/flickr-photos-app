package com.example.pix.ui.screens

import kotlinx.serialization.Serializable

@Serializable
data object Main

@Serializable
data class PictureDetail(
    val url: String,
    val title: String
)