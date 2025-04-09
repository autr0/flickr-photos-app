package com.example.pix.data.flickr

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pix.data.flickr.mapper.toPictureDbo
import com.example.pix.data.room.PictureDatabase
import com.example.pix.data.room.model.PictureDbo
import com.example.pix.data.room.model.RemoteKey
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class FlickrRemoteMediator(
    private val api: FlickrApi,
    private val db: PictureDatabase,
    val searchQuery: String
) : RemoteMediator<Int, PictureDbo>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PictureDbo>
    ): MediatorResult {
        return try {
            val loadKey = when(loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state = state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state = state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state = state)
                    val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextKey
                }
            }

            val photos = api.search(text = searchQuery, page = loadKey, count = 20).photos

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.getRemoteKeyDao().clearRemoteKeys()
                    db.getPictureDao().clearAll()
                }

                photos?.let { photosDto ->
                    val pictureDbo = photosDto.photo.map { it.toPictureDbo() }

                    val prevKey = if (photos.page - 1 > 0) photos.page - 1 else null
                    val nextKey = if (photos.page + 1 <= photos.pages) photos.page + 1 else null

                    val remoteKeys = pictureDbo.map { pictureDb ->
                        RemoteKey(
                            pictureId = pictureDb.id,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    }

                    db.getPictureDao().insertAll(pictureDbo)
                    db.getRemoteKeyDao().insertAll(remoteKeys)
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = photos?.pages == photos?.page
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PictureDbo>): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { pictureDbo ->
                db.getRemoteKeyDao().getRemoteKeyById(pictureDbo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PictureDbo>): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pictureDbo ->
                db.getRemoteKeyDao().getRemoteKeyById(pictureDbo.id)

            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PictureDbo>
    ): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.getRemoteKeyDao().getRemoteKeyById(id)
            }
        }
    }
}