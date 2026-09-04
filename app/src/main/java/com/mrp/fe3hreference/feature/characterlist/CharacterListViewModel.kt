package com.mrp.fe3hreference.feature.characterlist

import androidx.lifecycle.ViewModel
import com.mrp.fe3hreference.domain.repository.FE3HRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CharacterListViewModel(
    private val repository: FE3HRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<CharacterListUiState>(CharacterListUiState.Loading)
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    init {
        loadCharacters()
    }

    @Suppress("TooGenericExceptionCaught")
    private fun loadCharacters() {
        // Bundled FE3H data can fail in several distinct ways (malformed JSON,
        // unresolved cross-references, unknown enum values); all are treated
        // alike as a data-loading failure surfaced to the user.
        _uiState.value =
            try {
                CharacterListUiState.Success(repository.getCharacters())
            } catch (e: Exception) {
                CharacterListUiState.Error(e.message ?: "Unable to load characters")
            }
    }
}
