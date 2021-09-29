package com.kivinus.rickandmorty.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kivinus.rickandmorty.api.RamApiService
import com.kivinus.rickandmorty.api.response.RamApiCharacterResponse
import retrofit2.HttpException
import java.io.IOException

class CharactersPagingSource
    (
    private val apiService: RamApiService
) : PagingSource<Int, RamApiCharacterResponse>() {

    private companion object {
        const val PAGE_SIZE = 20
    }

    override fun getRefreshKey(state: PagingState<Int, RamApiCharacterResponse>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RamApiCharacterResponse> {
        val page: Int = params.key ?: 1

        return try {

            // requests
            val characters = apiService.getAllCharacters(page = page).listApiCharacters

            // return page
            LoadResult.Page(
                data = characters,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (characters.size < PAGE_SIZE) null else page + 1
            )

        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        }
    }

}