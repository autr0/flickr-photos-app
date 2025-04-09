package com.example.pix.di

import android.content.Context
import androidx.room.Room
import com.example.pix.data.room.PictureDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBeerDatabase(@ApplicationContext context: Context): PictureDatabase {
        return Room.databaseBuilder(
            context,
            PictureDatabase::class.java,
            "picture.db"
        ).build()
    }

}