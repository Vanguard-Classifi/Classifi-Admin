package com.khalidtouch.classifiadmin.data.util

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.classifiadmin.model.Country
import com.khalidtouch.classifiadmin.model.PagedCountry
import javax.inject.Inject

class ReadCountriesPagingSource @Inject constructor(
    private val userRepository: UserRepository,
) : PagingSource<Int, Country>() {
    val TAG = "ReadCo"
    override fun getRefreshKey(state: PagingState<Int, Country>): Int? =
        ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2).coerceAtLeast(0)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Country> {
        val currentPage = params.key ?: 1
        val resource = userRepository.getCountriesFromJson(
            page = currentPage,
            limit = params.loadSize,
        )

        return try {
            LoadResult.Page(
                data = resource.countries,
                prevKey = null,
                nextKey =
                if (currentPage <= (resource.totalSize / params.loadSize))
                    currentPage + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
