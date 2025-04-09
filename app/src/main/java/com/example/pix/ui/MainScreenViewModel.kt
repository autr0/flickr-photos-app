package com.example.pix.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.pix.domain.use_cases.SearchPictureUseCase
import com.example.pix.ui.mappers.toPictureView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val searchPictureUseCase: SearchPictureUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("cats")
    val query = _query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    @FlowPreview
    val pictures = _query.filter { it.isNotBlank() }
        .debounce(1000)
        .flatMapLatest { q ->
            searchPictureUseCase(q).map { pagingData ->
                pagingData.map { picture ->
                    picture.toPictureView()
                }

            }
        }.cachedIn(viewModelScope)


    fun updateQuery(q: String) {
        _query.update { q }
    }

    fun clearQuery() {
        _query.update { "" }
    }
}