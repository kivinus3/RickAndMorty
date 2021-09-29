package com.kivinus.rickandmorty.api.response

data class RamApiResponseInfo(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: Any
)