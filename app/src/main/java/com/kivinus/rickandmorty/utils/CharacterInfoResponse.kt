package com.kivinus.rickandmorty.utils

import com.kivinus.rickandmorty.model.CharacterDetailInfo

sealed class CharacterInfoResponse {
    data class Success(
        val character: CharacterDetailInfo
    ) : CharacterInfoResponse()

    object Error : CharacterInfoResponse()
    object Loading : CharacterInfoResponse()
}
