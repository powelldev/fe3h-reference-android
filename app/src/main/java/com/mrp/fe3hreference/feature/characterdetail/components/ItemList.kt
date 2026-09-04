package com.mrp.fe3hreference.feature.characterdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mrp.fe3hreference.data.model.Item

/**
 * Renders one list of items — reused for lost items, liked gifts, and disliked gifts
 * since they all share the same `Item` shape.
 */
@Composable
fun ItemList(
    items: List<Item>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items.forEach { item -> ItemRow(item = item) }
    }
}

@Composable
fun ItemRow(
    item: Item,
    modifier: Modifier = Modifier,
) {
    Text(
        text = item.name,
        style = MaterialTheme.typography.bodyMedium,
        modifier =
            modifier
                .testTag("item_row_${item.id.value}")
                .fillMaxWidth()
                .padding(vertical = 4.dp),
    )
}
