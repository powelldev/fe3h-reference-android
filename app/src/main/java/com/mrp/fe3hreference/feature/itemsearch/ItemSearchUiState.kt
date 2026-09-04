package com.mrp.fe3hreference.feature.itemsearch

import com.mrp.fe3hreference.data.model.Character
import com.mrp.fe3hreference.data.model.Item

data class ItemSearchUiState(
    val query: String = "",
    val results: List<LostItemSearchResult> = emptyList(),
)

data class LostItemSearchResult(
    val item: Item,
    val owner: Character,
)
