package com.example.pix.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pix.data.room.model.PictureDbo
import com.example.pix.data.room.model.RemoteKey

@Database(entities = [PictureDbo::class, RemoteKey::class], version = 1)
abstract class PictureDatabase: RoomDatabase() {
    abstract fun getPictureDao(): PictureDao
    abstract fun getRemoteKeyDao(): RemoteKeyDao
}