package com.mrp.fe3hreference.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Locale

/**
 * Placeholder portrait until real character artwork is bundled: a circle with the
 * character's first initial.
 */
@Composable
fun Portrait(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
) {
    Box(
        modifier =
            modifier
                .size(size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = name.take(1).uppercase(Locale.getDefault()),
            style = textStyle,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}
