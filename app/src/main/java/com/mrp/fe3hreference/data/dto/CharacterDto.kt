package com.mrp.fe3hreference.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharactersFileDto(
    val characters: List<CharacterDto>,
)

@Serializable
data class CharacterDto(
    val id: String,
    val name: String,
    val portrait: String,
    val majorCrestId: String? = null,
    val minorCrestId: String? = null,
    val proficiencies: Map<String, String>,
    val stats: CharacterStatsDto,
    val lostItemIds: List<String> = emptyList(),
    val likedGiftIds: List<String> = emptyList(),
    val dislikedGiftIds: List<String> = emptyList(),
    val favoriteTeaIds: List<String> = emptyList(),
    val teaTopicIds: List<String> = emptyList(),
    val teaQuestions: List<TeaQuestionDto> = emptyList(),
)

@Serializable
data class CharacterStatsDto(
    val base: Map<String, Int>,
    val growthRates: Map<String, Int>,
    val maximum: Map<String, Int>,
)

@Serializable
data class TeaQuestionDto(
    val comment: String,
    val validAnswers: List<String>,
)
