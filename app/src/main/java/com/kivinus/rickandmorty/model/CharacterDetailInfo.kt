package com.kivinus.rickandmorty.model

data class CharacterDetailInfo(
    val image: String,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val origin: String,
    val locationName: String,
    val locationType: String,
    val locationDimension: String,
    val episodes: Int
)
