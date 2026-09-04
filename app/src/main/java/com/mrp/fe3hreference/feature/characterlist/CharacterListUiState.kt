package com.mrp.fe3hreference.feature.characterlist

import com.mrp.fe3hreference.data.model.Character

sealed interface CharacterListUiState {
    data object Loading : CharacterListUiState

    data class Success(
        val characters: List<Character>,
    ) : CharacterListUiState

    data class Error(
        val message: String,
    ) : CharacterListUiState
}
