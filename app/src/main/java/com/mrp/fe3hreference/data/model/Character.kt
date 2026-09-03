package com.mrp.fe3hreference.data.model

data class Character(
    val id: CharacterId,
    val name: String,
    val portrait: String,
    val crests: List<Crest>,
    val proficiencies: Map<ProficiencyType, ProficiencyStatus>,
    val stats: CharacterStats,
    val lostItems: List<Item>,
    val likedGifts: List<Item>,
    val dislikedGifts: List<Item>,
    val favoriteTeas: List<Tea>,
    val teaTopics: List<TeaTopic>,
    val teaQuestions: List<TeaQuestion>,
)
