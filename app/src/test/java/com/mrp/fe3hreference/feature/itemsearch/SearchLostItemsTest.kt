package com.mrp.fe3hreference.feature.itemsearch

import com.mrp.fe3hreference.data.model.Character
import com.mrp.fe3hreference.data.model.CharacterId
import com.mrp.fe3hreference.data.model.CharacterStats
import com.mrp.fe3hreference.data.model.Item
import com.mrp.fe3hreference.data.model.ItemId
import com.mrp.fe3hreference.data.model.LostItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private fun testCharacter(
    id: String,
    name: String,
): Character =
    Character(
        id = CharacterId(id),
        name = name,
        portrait = id,
        crests = emptyList(),
        proficiencies = emptyMap(),
        stats = CharacterStats(base = emptyMap(), growthRates = emptyMap(), maximum = emptyMap()),
        lostItems = emptyList(),
        likedGifts = emptyList(),
        dislikedGifts = emptyList(),
        favoriteTeas = emptyList(),
        teaTopics = emptyList(),
        teaQuestions = emptyList(),
    )

class SearchLostItemsTest {
    private val dimitri = testCharacter("dimitri", "Dimitri")
    private val overcoat = Item(id = ItemId("tattered_overcoat"), name = "Tattered Overcoat")
    private val lostItems = listOf(LostItem(item = overcoat, ownerId = dimitri.id))
    private val charactersById = mapOf(dimitri.id to dimitri)

    @Test
    fun `blank query returns no results`() {
        assertTrue(searchLostItems(lostItems, charactersById, "").isEmpty())
    }

    @Test
    fun `matches item name case insensitively`() {
        val results = searchLostItems(lostItems, charactersById, "OVERCOAT")

        assertEquals(1, results.size)
        assertEquals(dimitri, results.single().owner)
    }

    @Test
    fun `substring match finds the item`() {
        val results = searchLostItems(lostItems, charactersById, "tatter")

        assertEquals(overcoat, results.single().item)
    }

    @Test
    fun `no match returns an empty list`() {
        assertTrue(searchLostItems(lostItems, charactersById, "no such item").isEmpty())
    }

    @Test
    fun `an item whose owner cannot be resolved is skipped rather than crashing`() {
        val orphanedItem = LostItem(item = overcoat, ownerId = CharacterId("unknown"))

        val results = searchLostItems(listOf(orphanedItem), emptyMap(), "overcoat")

        assertTrue(results.isEmpty())
    }
}
