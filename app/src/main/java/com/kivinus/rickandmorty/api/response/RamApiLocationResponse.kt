package com.kivinus.rickandmorty.api.response

data class RamApiLocationResponse(
    val created: String,
    val dimension: String,
    val id: Int,
    val name: String,
    val residents: List<String>,
    val type: String,
    val url: String
)