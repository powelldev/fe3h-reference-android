package com.mrp.fe3hreference.feature.itemsearch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mrp.fe3hreference.feature.itemsearch.components.ItemSearchBar
import com.mrp.fe3hreference.feature.itemsearch.components.ItemSearchResultList
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSearchScreen(
    onBackClick: () -> Unit = {},
    onCharacterClick: (String) -> Unit = {},
    viewModel: ItemSearchViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Item Search") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            ItemSearchBar(query = state.query, onQueryChanged = viewModel::onQueryChanged)

            when {
                state.query.isBlank() -> {
                    ItemSearchHint(text = "Type an item's name to find who lost it.")
                }

                state.results.isEmpty() -> {
                    ItemSearchHint(text = "No items found.", testTag = "item_search_no_results")
                }

                else -> {
                    ItemSearchResultList(
                        results = state.results,
                        onResultClick = onCharacterClick,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemSearchHint(
    text: String,
    testTag: String = "item_search_hint",
) {
    Box(
        modifier = Modifier.fillMaxSize().padding(top = 32.dp).testTag(testTag),
        contentAlignment = Alignment.TopCenter,
    ) {
        Text(text = text, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
