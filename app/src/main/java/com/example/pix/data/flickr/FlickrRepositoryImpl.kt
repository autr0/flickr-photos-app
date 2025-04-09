package com.example.pix.data.flickr

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.pix.data.flickr.mapper.toPicture
import com.example.pix.data.room.PictureDatabase
import com.example.pix.domain.entity.Picture
import com.example.pix.domain.repository.FlickrRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class FlickrRepositoryImpl(
    private val flickrApi: FlickrApi,
    private val pictureDatabase: PictureDatabase
) : FlickrRepository {

    override fun search(searchQuery: String): Flow<PagingData<Picture>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = FlickrRemoteMediator(
                db = pictureDatabase,
                api = flickrApi,
                searchQuery = searchQuery
            ),
            pagingSourceFactory = {
                pictureDatabase.getPictureDao().pagingSource()
            }).flow
            .map { pagingData ->
                pagingData.map { pictureDbo -> pictureDbo.toPicture() }
            }
    }

}