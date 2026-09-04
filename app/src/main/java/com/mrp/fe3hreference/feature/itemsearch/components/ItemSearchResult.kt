package com.mrp.fe3hreference.feature.itemsearch.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mrp.fe3hreference.feature.itemsearch.LostItemSearchResult
import com.mrp.fe3hreference.ui.components.Portrait

@Composable
fun ItemSearchResultList(
    results: List<LostItemSearchResult>,
    onResultClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.testTag("item_search_results")) {
        items(results, key = { it.item.id.value }) { result ->
            ItemSearchResultRow(result = result, onClick = { onResultClick(result.owner.id.value) })
        }
    }
}

@Composable
fun ItemSearchResultRow(
    result: LostItemSearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .testTag("item_search_result_${result.item.id.value}")
                .clickable(onClick = onClick)
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Portrait(name = result.owner.name, size = 40.dp, textStyle = MaterialTheme.typography.titleSmall)
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = result.item.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = result.owner.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
