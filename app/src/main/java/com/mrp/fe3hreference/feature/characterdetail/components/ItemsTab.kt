package com.mrp.fe3hreference.feature.characterdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mrp.fe3hreference.data.model.Character
import com.mrp.fe3hreference.data.model.Item

@Composable
fun ItemsTab(
    character: Character,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .testTag("items_tab")
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ItemSection(title = "Lost Items", items = character.lostItems, testTag = "lost_items")
        ItemSection(title = "Liked Gifts", items = character.likedGifts, testTag = "liked_gifts")
        ItemSection(title = "Disliked Gifts", items = character.dislikedGifts, testTag = "disliked_gifts")
    }
}

@Composable
private fun ItemSection(
    title: String,
    items: List<Item>,
    testTag: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        if (items.isEmpty()) {
            Text(
                text = "None",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.testTag("${testTag}_empty"),
            )
        } else {
            ItemList(items = items, modifier = Modifier.testTag(testTag))
        }
    }
}
