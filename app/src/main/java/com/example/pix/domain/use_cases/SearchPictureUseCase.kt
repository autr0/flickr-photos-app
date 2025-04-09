package com.example.pix.domain.use_cases

import androidx.paging.PagingData
import com.example.pix.domain.entity.Picture
import com.example.pix.domain.repository.FlickrRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPictureUseCase @Inject constructor(
    private val repo: FlickrRepository
) {

    operator fun invoke(searchQuery: String): Flow<PagingData<Picture>> {
        return repo.search(searchQuery = searchQuery)
    }

}