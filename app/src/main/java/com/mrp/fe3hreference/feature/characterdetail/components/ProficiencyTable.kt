package com.mrp.fe3hreference.feature.characterdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mrp.fe3hreference.data.model.ProficiencyStatus
import com.mrp.fe3hreference.data.model.ProficiencyType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProficiencyTable(
    proficiencies: Map<ProficiencyType, ProficiencyStatus>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.testTag("proficiency_table"),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ProficiencyType.entries.forEach { type ->
            val status = proficiencies[type] ?: ProficiencyStatus.Neutral
            Column(
                modifier = Modifier.widthIn(min = 56.dp).testTag("proficiency_${type.name}"),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = type.name,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
                Text(
                    text = proficiencySymbol(status),
                    style = MaterialTheme.typography.titleMedium,
                    color = proficiencyColor(status),
                )
            }
        }
    }
}

internal fun proficiencySymbol(status: ProficiencyStatus): String =
    when (status) {
        ProficiencyStatus.Neutral -> ""
        ProficiencyStatus.Boon -> "▲"
        ProficiencyStatus.Bane -> "▼"
        ProficiencyStatus.BuddingTalent -> "★★★"
    }

private val BoonColor = Color(0xFF1565C0)
private val BaneColor = Color(0xFFC62828)
private val BuddingTalentColor = Color(0xFFF9A825)

internal fun proficiencyColor(status: ProficiencyStatus): Color =
    when (status) {
        ProficiencyStatus.Neutral -> Color.Unspecified
        ProficiencyStatus.Boon -> BoonColor
        ProficiencyStatus.Bane -> BaneColor
        ProficiencyStatus.BuddingTalent -> BuddingTalentColor
    }
