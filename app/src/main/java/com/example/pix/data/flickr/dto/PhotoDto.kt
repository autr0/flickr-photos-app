package com.example.pix.data.flickr.dto

data class PhotoDto(
    val farm: Int,
    val id: String,
    val isfamily: Int,
    val isfriend: Int,
    val ispublic: Int,
    val owner: String,
    val secret: String,
    val server: String, // for url
    val title: String
)

/*
*
* Default 500px image url -->
* https://live.staticflickr.com/{server-id}/{id}_{secret}.jpg
*
* Specific image px size -->
* https://live.staticflickr.com/{server-id}/{id}_{secret}_{size-suffix}.jpg
*
* */