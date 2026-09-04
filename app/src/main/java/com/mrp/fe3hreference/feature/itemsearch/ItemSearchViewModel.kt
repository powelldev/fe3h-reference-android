package com.mrp.fe3hreference.feature.itemsearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrp.fe3hreference.data.model.Character
import com.mrp.fe3hreference.data.model.CharacterId
import com.mrp.fe3hreference.data.model.LostItem
import com.mrp.fe3hreference.domain.repository.FE3HRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItemSearchViewModel(
    private val repository: FE3HRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ItemSearchUiState())
    val uiState: StateFlow<ItemSearchUiState> = _uiState.asStateFlow()

    private var lostItems: List<LostItem> = emptyList()
    private var charactersById: Map<CharacterId, Character> = emptyMap()

    init {
        viewModelScope.launch {
            charactersById = repository.getCharacters().associateBy { it.id }
            lostItems = repository.getLostItems()
            onQueryChanged(_uiState.value.query)
        }
    }

    fun onQueryChanged(query: String) {
        _uiState.update { state ->
            state.copy(query = query, results = searchLostItems(lostItems, charactersById, query))
        }
    }
}

fun searchLostItems(
    lostItems: List<LostItem>,
    charactersById: Map<CharacterId, Character>,
    query: String,
): List<LostItemSearchResult> {
    if (query.isBlank()) return emptyList()

    return lostItems
        .filter { it.item.name.contains(query, ignoreCase = true) }
        .mapNotNull { lostItem ->
            charactersById[lostItem.ownerId]?.let { owner -> LostItemSearchResult(item = lostItem.item, owner = owner) }
        }
}
