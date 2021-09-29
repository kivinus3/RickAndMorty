package com.kivinus.rickandmorty.api.response

data class RamApiCharacterResponse(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: RamCharacterResponseLocation,
    val name: String,
    val origin: RamCharacterResponseOrigin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)