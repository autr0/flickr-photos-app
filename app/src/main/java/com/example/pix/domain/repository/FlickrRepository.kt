package com.example.pix.domain.repository

import androidx.paging.PagingData
import com.example.pix.domain.entity.Picture
import kotlinx.coroutines.flow.Flow

interface FlickrRepository {

     fun search(searchQuery: String): Flow<PagingData<Picture>>

}