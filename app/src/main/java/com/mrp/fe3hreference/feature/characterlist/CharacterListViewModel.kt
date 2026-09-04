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

    private fun loadCharacters() {
        _uiState.value =
            try {
                CharacterListUiState.Success(repository.getCharacters())
            } catch (e: Exception) {
                CharacterListUiState.Error(e.message ?: "Unable to load characters")
            }
    }
}
