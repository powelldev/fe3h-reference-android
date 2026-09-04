package com.mrp.fe3hreference.feature.characterdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mrp.fe3hreference.data.model.CharacterId
import com.mrp.fe3hreference.feature.characterdetail.components.ItemsTab
import com.mrp.fe3hreference.feature.characterdetail.components.StatsTab
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: String,
    onBackClick: () -> Unit = {},
    viewModel: CharacterDetailViewModel =
        koinViewModel(parameters = { parametersOf(CharacterId(characterId)) }),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = (state as? CharacterDetailUiState.Success)?.character?.name ?: "Character Detail"
                    Text(title)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←")
                    }
                },
            )
        },
    ) { padding ->
        when (val currentState = state) {
            is CharacterDetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is CharacterDetailUiState.Success -> {
                Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                    TabRow(selectedTabIndex = currentState.selectedTab.ordinal) {
                        CharacterDetailTab.entries.forEach { tab ->
                            Tab(
                                selected = tab == currentState.selectedTab,
                                onClick = { viewModel.onTabSelected(tab) },
                                text = { Text(tab.label) },
                                modifier = Modifier.testTag("character_detail_tab_${tab.name}"),
                            )
                        }
                    }

                    when (currentState.selectedTab) {
                        CharacterDetailTab.STATS -> StatsTab(character = currentState.character)
                        CharacterDetailTab.ITEMS -> ItemsTab(character = currentState.character)
                        CharacterDetailTab.TEAS -> PlaceholderTabContent(text = "Teas — coming soon")
                    }
                }
            }

            is CharacterDetailUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = currentState.message)
                }
            }
        }
    }
}

@Composable
private fun PlaceholderTabContent(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = text)
    }
}
