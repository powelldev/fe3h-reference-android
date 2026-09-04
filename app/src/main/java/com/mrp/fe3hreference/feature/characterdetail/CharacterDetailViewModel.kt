package com.mrp.fe3hreference.feature.characterdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrp.fe3hreference.data.model.CharacterId
import com.mrp.fe3hreference.domain.repository.FE3HRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    characterId: CharacterId,
    private val repository: FE3HRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { loadCharacter(characterId) }
    }

    @Suppress("TooGenericExceptionCaught")
    private suspend fun loadCharacter(characterId: CharacterId) {
        // Bundled FE3H data can fail in several distinct ways (malformed JSON,
        // unresolved cross-references, unknown enum values); all are treated
        // alike as a data-loading failure surfaced to the user.
        _uiState.value =
            try {
                val character = repository.getCharacter(characterId)
                if (character != null) {
                    CharacterDetailUiState.Success(character)
                } else {
                    CharacterDetailUiState.Error("Character not found: ${characterId.value}")
                }
            } catch (e: Exception) {
                CharacterDetailUiState.Error(e.message ?: "Unable to load character")
            }
    }

    fun onTabSelected(tab: CharacterDetailTab) {
        _uiState.update { state ->
            if (state is CharacterDetailUiState.Success) state.copy(selectedTab = tab) else state
        }
    }
}
