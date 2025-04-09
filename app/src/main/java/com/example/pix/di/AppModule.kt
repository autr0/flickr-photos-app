package com.example.pix.di

import com.example.pix.data.flickr.FlickrApi
import com.example.pix.data.flickr.FlickrRepositoryImpl
import com.example.pix.data.room.PictureDatabase
import com.example.pix.domain.repository.FlickrRepository
import com.example.pix.domain.use_cases.SearchPictureUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFlickrRepository(api: FlickrApi, db: PictureDatabase): FlickrRepository {
        return FlickrRepositoryImpl(flickrApi = api, pictureDatabase = db)
    }

    @Provides
    @Singleton
    fun provideSearchPictureUseCase(repository: FlickrRepository): SearchPictureUseCase {
        return SearchPictureUseCase(repo = repository)
    }
}