package com.kivinus.rickandmorty.api.response

import com.google.gson.annotations.SerializedName

data class RamApiResponse(
    val info: RamApiResponseInfo,
    @SerializedName("results")
    val listApiCharacters: List<RamApiCharacterResponse>
)