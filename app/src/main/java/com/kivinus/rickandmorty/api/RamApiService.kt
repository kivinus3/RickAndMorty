package com.kivinus.rickandmorty.api

import com.kivinus.rickandmorty.api.response.RamApiCharacterResponse
import com.kivinus.rickandmorty.api.response.RamApiLocationResponse
import com.kivinus.rickandmorty.api.response.RamApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RamApiService {

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }

    @GET("character")
    suspend fun getAllCharacters(
        @Query("page") page: Int
    ): RamApiResponse

    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") characterId: Int
    ): RamApiCharacterResponse

    @GET("location/{id}")
    suspend fun getLocationById(
        @Path("id") locationId: Int
    ): RamApiLocationResponse


}