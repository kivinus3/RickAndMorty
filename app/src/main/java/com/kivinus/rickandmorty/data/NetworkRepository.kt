package com.kivinus.rickandmorty.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.kivinus.rickandmorty.api.RamApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository
@Inject constructor(
    private val ramApi: RamApiService
) {

    fun getAllCharacters() =
        Pager(
            config = PagingConfig(
                initialLoadSize = 20,
                enablePlaceholders = false,
                pageSize = 20,
                maxSize = 60
            ),
            pagingSourceFactory = { CharactersPagingSource(ramApi) }
        ).flow

    suspend fun getCharacterInfoById(id: Int) = ramApi.getCharacterById(id)

    suspend fun getLocationByUrl(url: String) =
        ramApi.getLocationById(url.substringAfterLast('/').toInt())


}