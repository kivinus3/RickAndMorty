package com.kivinus.rickandmorty.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kivinus.rickandmorty.utils.CharacterInfoResponse
import com.kivinus.rickandmorty.data.NetworkRepository
import com.kivinus.rickandmorty.model.CharacterDetailInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CharacterDetailViewModel @Inject constructor
    (private val repoNetwork: NetworkRepository) : ViewModel() {

    private val _response: MutableStateFlow<CharacterInfoResponse> = MutableStateFlow(
        CharacterInfoResponse.Loading)
    val response: StateFlow<CharacterInfoResponse> = _response

    fun fetchCharacterByID(id: Int) {
        _response.value = CharacterInfoResponse.Loading
        viewModelScope.launch {
            try {
                val character = repoNetwork.getCharacterInfoById(id)
                val location = repoNetwork.getLocationByUrl(character.location.url)
                val info = CharacterDetailInfo(
                    character.image,
                    character.name,
                    character.status,
                    character.species,
                    character.gender,
                    character.origin.name,
                    location.name,
                    location.type,
                    location.dimension,
                    character.episode.size
                )
                _response.value = CharacterInfoResponse.Success(info)
            } catch (e: Exception) {
                _response.value = CharacterInfoResponse.Error
            }
        }
    }


}