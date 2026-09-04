package com.mrp.fe3hreference.feature.characterdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.mrp.fe3hreference.data.model.Character
import com.mrp.fe3hreference.data.model.Tea
import com.mrp.fe3hreference.data.model.TeaQuestion
import com.mrp.fe3hreference.data.model.TeaTopic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeasTab(
    character: Character,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSearching = searchQuery.isNotBlank()
    val filteredTopics = filterTeaTopics(character.teaTopics, searchQuery)
    val filteredQuestions = filterTeaQuestions(character.teaQuestions, searchQuery)

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .testTag("teas_tab")
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            placeholder = { Text("Search tea topics and final questions…") },
            singleLine = true,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag("tea_search_field"),
        )

        if (!isSearching) {
            FavoriteTeasSection(teas = character.favoriteTeas)
        }

        if (!isSearching || filteredTopics.isNotEmpty()) {
            TeaTopicsSection(topics = filteredTopics)
        }

        if (!isSearching || filteredQuestions.isNotEmpty()) {
            TeaQuestionsSection(questions = filteredQuestions)
        }

        if (isSearching && filteredTopics.isEmpty() && filteredQuestions.isEmpty()) {
            Text(
                text = "No results",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.testTag("tea_search_empty"),
            )
        }
    }
}

@Composable
private fun FavoriteTeasSection(teas: List<Tea>) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Favorite Teas", style = MaterialTheme.typography.titleMedium)
        if (teas.isEmpty()) {
            Text(
                text = "None",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.testTag("favorite_teas_empty"),
            )
        } else {
            teas.forEach { tea ->
                Text(
                    text = tea.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag("favorite_tea_${tea.id.value}"),
                )
            }
        }
    }
}

@Composable
private fun TeaTopicsSection(topics: List<TeaTopic>) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Tea Topics", style = MaterialTheme.typography.titleMedium)
        if (topics.isEmpty()) {
            Text(
                text = "None",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.testTag("tea_topics_empty"),
            )
        } else {
            topics.forEach { topic ->
                Text(
                    text = topic.topic,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag("tea_topic_${topic.id.value}"),
                )
            }
        }
    }
}

@Composable
private fun TeaQuestionsSection(questions: List<TeaQuestion>) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Final Questions", style = MaterialTheme.typography.titleMedium)
        if (questions.isEmpty()) {
            Text(
                text = "None",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.testTag("tea_questions_empty"),
            )
        } else {
            questions.forEachIndexed { index, question ->
                Column(
                    modifier = Modifier.fillMaxWidth().testTag("tea_question_$index"),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(text = question.comment, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = question.validAnswers.joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

fun filterTeaTopics(
    topics: List<TeaTopic>,
    query: String,
): List<TeaTopic> = if (query.isBlank()) topics else topics.filter { it.topic.contains(query, ignoreCase = true) }

fun filterTeaQuestions(
    questions: List<TeaQuestion>,
    query: String,
): List<TeaQuestion> =
    if (query.isBlank()) {
        questions
    } else {
        questions.filter { question ->
            question.comment.contains(query, ignoreCase = true) ||
                question.validAnswers.any { it.contains(query, ignoreCase = true) }
        }
    }
