package com.mrp.fe3hreference.feature.characterdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mrp.fe3hreference.data.model.Crest
import java.util.Locale

@Composable
fun CrestList(
    crests: List<Crest>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.testTag("crest_list"),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        crests.forEach { crest -> CrestIcon(crest = crest) }
    }
}

@Composable
fun CrestIcon(
    crest: Crest,
    modifier: Modifier = Modifier,
) {
    var showTooltip by remember { mutableStateOf(false) }

    Box(
        modifier =
            modifier
                .testTag("crest_icon_${crest.id.value}")
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { showTooltip = true },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = crest.name.take(1).uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
    }

    if (showTooltip) {
        AlertDialog(
            onDismissRequest = { showTooltip = false },
            confirmButton = {
                TextButton(onClick = { showTooltip = false }) {
                    Text("OK")
                }
            },
            title = { Text(crest.name) },
            text = { Text(crest.description) },
            modifier = Modifier.testTag("crest_tooltip_${crest.id.value}"),
        )
    }
}
