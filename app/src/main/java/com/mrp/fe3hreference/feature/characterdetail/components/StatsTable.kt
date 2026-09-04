package com.mrp.fe3hreference.feature.characterdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mrp.fe3hreference.data.model.StatType

/**
 * Renders one stat table (base, growth rates, or maximum) — reused for all three since
 * they share the same StatType -> Int shape.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatsTable(
    title: String,
    stats: Map<StatType, Int>,
    modifier: Modifier = Modifier,
    valueSuffix: String = "",
) {
    Column(modifier = modifier.fillMaxWidth().testTag("stats_table_$title")) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            StatType.entries.forEach { statType ->
                val value = stats[statType]
                if (value != null) {
                    Column(modifier = Modifier.width(56.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = statType.name, style = MaterialTheme.typography.labelSmall)
                        Text(text = "$value$valueSuffix", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
