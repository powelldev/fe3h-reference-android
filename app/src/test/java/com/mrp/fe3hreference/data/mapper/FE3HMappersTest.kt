package com.mrp.fe3hreference.data.mapper

import com.mrp.fe3hreference.data.dto.CharacterDto
import com.mrp.fe3hreference.data.dto.CharacterStatsDto
import com.mrp.fe3hreference.data.dto.CrestDto
import com.mrp.fe3hreference.data.dto.TeaDto
import com.mrp.fe3hreference.data.dto.TeaTopicDto
import com.mrp.fe3hreference.data.model.Item
import com.mrp.fe3hreference.data.model.ItemId
import com.mrp.fe3hreference.data.model.ProficiencyStatus
import com.mrp.fe3hreference.data.model.ProficiencyType
import com.mrp.fe3hreference.data.model.StatType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class FE3HMappersTest {
    @Test
    fun `parseProficiencyStatus maps every known status`() {
        assertEquals(ProficiencyStatus.Neutral, parseProficiencyStatus("NEUTRAL"))
        assertEquals(ProficiencyStatus.Boon, parseProficiencyStatus("BOON"))
        assertEquals(ProficiencyStatus.Bane, parseProficiencyStatus("BANE"))
        assertEquals(ProficiencyStatus.BuddingTalent, parseProficiencyStatus("BUDDING_TALENT"))
    }

    @Test
    fun `parseProficiencyStatus throws on unknown status`() {
        assertThrows(IllegalStateException::class.java) { parseProficiencyStatus("UNKNOWN") }
    }

    @Test
    fun `CharacterStatsDto toDomain converts each stat map by StatType`() {
        val dto =
            CharacterStatsDto(
                base = mapOf("HP" to 22, "STR" to 6),
                growthRates = mapOf("HP" to 35, "STR" to 30),
                maximum = mapOf("HP" to 67, "STR" to 43),
            )

        val stats = dto.toDomain()

        assertEquals(22, stats.base[StatType.HP])
        assertEquals(6, stats.base[StatType.STR])
        assertEquals(30, stats.growthRates[StatType.STR])
        assertEquals(43, stats.maximum[StatType.STR])
    }

    @Test
    fun `CharacterDto toDomain resolves crests, items, teas and topics by id`() {
        val dto = hubertDto()
        val hresvelgTreatise = Item(ItemId("hresvelg_treatise"), "Hresvelg Treatise")
        val coffeeBeans = Item(ItemId("coffee_beans"), "Coffee Beans")
        val legendsOfChivalry = Item(ItemId("legends_of_chivalry"), "Legends of Chivalry")
        val itemsById = listOf(hresvelgTreatise, coffeeBeans, legendsOfChivalry).associateBy { it.id }
        val flames = CrestDto("flames", "Flames", "desc").toDomain()
        val crestsById = mapOf(flames.id to flames)
        val dagdaFruitBlend = TeaDto("dagda_fruit_blend", "Dagda Fruit Blend").toDomain()
        val teasById = mapOf(dagdaFruitBlend.id to dagdaFruitBlend)
        val aNewGambit = TeaTopicDto("a_new_gambit", "A new gambit").toDomain()
        val teaTopicsById = mapOf(aNewGambit.id to aNewGambit)

        val character = dto.toDomain(itemsById, crestsById, teasById, teaTopicsById)

        assertEquals("hubert", character.id.value)
        assertEquals(listOf(flames), character.crests)
        assertEquals(listOf(hresvelgTreatise), character.lostItems)
        assertEquals(listOf(coffeeBeans), character.likedGifts)
        assertEquals(listOf(legendsOfChivalry), character.dislikedGifts)
        assertEquals(listOf(dagdaFruitBlend), character.favoriteTeas)
        assertEquals(listOf(aNewGambit), character.teaTopics)
        assertEquals(ProficiencyStatus.BuddingTalent, character.proficiencies[ProficiencyType.LANCE])
        assertEquals(ProficiencyStatus.Bane, character.proficiencies[ProficiencyType.AXE])
        assertEquals(ProficiencyStatus.Boon, character.proficiencies[ProficiencyType.BOW])
        assertEquals(ProficiencyStatus.Neutral, character.proficiencies[ProficiencyType.SWORD])
    }

    @Test
    fun `CharacterDto toDomain throws when a referenced item id is missing`() {
        val dto = hubertDto()

        assertThrows(IllegalStateException::class.java) {
            dto.toDomain(emptyMap(), emptyMap(), emptyMap(), emptyMap())
        }
    }

    private fun hubertDto(): CharacterDto =
        CharacterDto(
            id = "hubert",
            name = "Hubert",
            portrait = "hubert",
            majorCrestId = "flames",
            minorCrestId = null,
            proficiencies =
                mapOf(
                    "SWORD" to "NEUTRAL",
                    "LANCE" to "BUDDING_TALENT",
                    "AXE" to "BANE",
                    "BOW" to "BOON",
                ),
            stats =
                CharacterStatsDto(
                    base = mapOf("HP" to 22),
                    growthRates = mapOf("HP" to 35),
                    maximum = mapOf("HP" to 67),
                ),
            lostItemIds = listOf("hresvelg_treatise"),
            likedGiftIds = listOf("coffee_beans"),
            dislikedGiftIds = listOf("legends_of_chivalry"),
            favoriteTeaIds = listOf("dagda_fruit_blend"),
            teaTopicIds = listOf("a_new_gambit"),
            teaQuestions = emptyList(),
        )
}
