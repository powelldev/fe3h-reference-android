package com.mrp.fe3hreference.navigation

sealed class Route(
    val route: String,
) {
    data object Characters : Route("characters")

    data object ItemSearch : Route("items/search")

    data object CharacterDetail : Route("character/{characterId}") {
        const val ARG_CHARACTER_ID = "characterId"

        fun createRoute(characterId: String) = "character/$characterId"
    }
}
