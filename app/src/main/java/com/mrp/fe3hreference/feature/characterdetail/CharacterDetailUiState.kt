package com.mrp.fe3hreference.feature.characterdetail

import com.mrp.fe3hreference.data.model.Character

sealed interface CharacterDetailUiState {
    data object Loading : CharacterDetailUiState

    data class Success(
        val character: Character,
        val selectedTab: CharacterDetailTab = CharacterDetailTab.STATS,
        val teaSearchQuery: String = "",
    ) : CharacterDetailUiState

    data class Error(
        val message: String,
    ) : CharacterDetailUiState
}
