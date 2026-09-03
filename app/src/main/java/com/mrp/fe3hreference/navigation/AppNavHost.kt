package com.mrp.fe3hreference.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mrp.fe3hreference.feature.characterdetail.CharacterDetailScreen
import com.mrp.fe3hreference.feature.characterlist.CharacterListScreen
import com.mrp.fe3hreference.feature.itemsearch.ItemSearchScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Route.Characters.route) {
        composable(Route.Characters.route) {
            CharacterListScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(Route.CharacterDetail.createRoute(characterId))
                },
                onItemSearchClick = {
                    navController.navigate(Route.ItemSearch.route)
                },
            )
        }
        composable(Route.CharacterDetail.route) { backStackEntry ->
            val characterId =
                backStackEntry.arguments
                    ?.getString(Route.CharacterDetail.ARG_CHARACTER_ID)
                    .orEmpty()
            CharacterDetailScreen(characterId = characterId)
        }
        composable(Route.ItemSearch.route) {
            ItemSearchScreen()
        }
    }
}
