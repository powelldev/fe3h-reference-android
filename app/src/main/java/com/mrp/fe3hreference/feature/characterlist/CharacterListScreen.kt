package com.mrp.fe3hreference.feature.characterlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mrp.fe3hreference.feature.characterlist.components.CharacterGridItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onCharacterClick: (String) -> Unit,
    onItemSearchClick: () -> Unit,
    viewModel: CharacterListViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { CharacterListTopBar(onItemSearchClick = onItemSearchClick) },
    ) { padding ->
        CharacterListContent(
            state = state,
            padding = padding,
            onCharacterClick = onCharacterClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterListTopBar(onItemSearchClick: () -> Unit) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("FE3H Reference") },
        actions = {
            IconButton(onClick = { menuExpanded = true }) {
                Text("⋮")
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                DropdownMenuItem(
                    text = { Text("Item Search") },
                    onClick = {
                        menuExpanded = false
                        onItemSearchClick()
                    },
                )
            }
        },
    )
}

@Composable
private fun CharacterListContent(
    state: CharacterListUiState,
    padding: PaddingValues,
    onCharacterClick: (String) -> Unit,
) {
    when (state) {
        is CharacterListUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is CharacterListUiState.Success -> {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 96.dp),
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .testTag("character_grid"),
                contentPadding = PaddingValues(16.dp),
            ) {
                items(state.characters, key = { it.id.value }) { character ->
                    CharacterGridItem(
                        character = character,
                        onClick = { onCharacterClick(character.id.value) },
                    )
                }
            }
        }

        is CharacterListUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = state.message)
            }
        }
    }
}
