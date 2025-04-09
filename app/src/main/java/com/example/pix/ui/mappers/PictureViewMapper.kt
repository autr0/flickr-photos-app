package com.example.pix.ui.mappers

import com.example.pix.domain.entity.Picture
import com.example.pix.ui.model.PictureView

fun Picture.toPictureView() = PictureView(
    url = url,
    title = title
)