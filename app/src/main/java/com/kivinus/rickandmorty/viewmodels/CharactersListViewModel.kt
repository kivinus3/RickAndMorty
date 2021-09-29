package com.kivinus.rickandmorty.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.kivinus.rickandmorty.data.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharactersListViewModel @Inject constructor
    (repoNetwork: NetworkRepository) : ViewModel() {

    var characters = repoNetwork.getAllCharacters().cachedIn(viewModelScope)

}