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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mrp.fe3hreference.data.model.Character
import com.mrp.fe3hreference.ui.components.Portrait

@Composable
fun StatsTab(
    character: Character,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .testTag("stats_tab")
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Portrait(
            name = character.name,
            size = 96.dp,
            textStyle = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.testTag("stats_tab_portrait"),
        )

        if (character.crests.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Crests", style = MaterialTheme.typography.titleMedium)
                CrestList(crests = character.crests)
            }
        }

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Proficiencies", style = MaterialTheme.typography.titleMedium)
            ProficiencyTable(proficiencies = character.proficiencies)
        }

        StatsTable(title = "Base Stats", stats = character.stats.base)
        StatsTable(title = "Growth Rates", stats = character.stats.growthRates, valueSuffix = "%")
        StatsTable(title = "Maximum Stats", stats = character.stats.maximum)
    }
}
