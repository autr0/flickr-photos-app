package com.example.pix.data.flickr.mapper

import com.example.pix.data.flickr.dto.PhotoDto
import com.example.pix.data.room.model.PictureDbo
import com.example.pix.domain.entity.Picture

// https://www.flickr.com/services/api/misc.urls.html

// by default 500px size
fun PhotoDto.toEntity(): Picture = Picture(
    title = title,
    url = "https://live.staticflickr.com/${server}/${id}_${secret}.jpg"
)

fun PhotoDto.toPictureDbo(): PictureDbo = PictureDbo(
    id = id,
    title = title,
    url = "https://live.staticflickr.com/${server}/${id}_${secret}.jpg"
)

fun PictureDbo.toPicture(): Picture = Picture(
    url = url,
    title = title
)
