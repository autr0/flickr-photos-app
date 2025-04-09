package com.example.pix.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    val pictureId: String,
    val prevKey: Int?,
    val nextKey: Int?
)