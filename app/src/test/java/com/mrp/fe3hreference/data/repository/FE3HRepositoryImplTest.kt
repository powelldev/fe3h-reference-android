package com.mrp.fe3hreference.data.repository

import com.mrp.fe3hreference.data.model.CharacterId
import com.mrp.fe3hreference.data.model.ProficiencyStatus
import com.mrp.fe3hreference.data.model.ProficiencyType
import com.mrp.fe3hreference.data.source.FE3HJsonParser
import com.mrp.fe3hreference.data.source.TestFE3HRawDataSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FE3HRepositoryImplTest {
    private lateinit var repository: FE3HRepositoryImpl

    @Before
    fun setUp() {
        repository = FE3HRepositoryImpl(TestFE3HRawDataSource(), FE3HJsonParser())
    }

    @Test
    fun `Hubert exists`() {
        val hubert = repository.getCharacter(CharacterId("hubert"))

        assertNotNull(hubert)
        assertEquals("Hubert", hubert?.name)
    }

    @Test
    fun `Hubert has Lance BuddingTalent`() {
        val hubert = repository.getCharacter(CharacterId("hubert"))!!

        assertEquals(ProficiencyStatus.BuddingTalent, hubert.proficiencies[ProficiencyType.LANCE])
    }

    @Test
    fun `Hubert has Axe Bane`() {
        val hubert = repository.getCharacter(CharacterId("hubert"))!!

        assertEquals(ProficiencyStatus.Bane, hubert.proficiencies[ProficiencyType.AXE])
    }

    @Test
    fun `Hubert has Bow Boon`() {
        val hubert = repository.getCharacter(CharacterId("hubert"))!!

        assertEquals(ProficiencyStatus.Boon, hubert.proficiencies[ProficiencyType.BOW])
    }

    @Test
    fun `Hubert has Sword Neutral`() {
        val hubert = repository.getCharacter(CharacterId("hubert"))!!

        assertEquals(ProficiencyStatus.Neutral, hubert.proficiencies[ProficiencyType.SWORD])
    }

    @Test
    fun `Hubert resolves his lost items, gifts, teas and tea topics by id`() {
        val hubert = repository.getCharacter(CharacterId("hubert"))!!

        assertTrue(hubert.lostItems.any { it.id.value == "hresvelg_treatise" })
        assertTrue(hubert.likedGifts.any { it.id.value == "coffee_beans" })
        assertTrue(hubert.dislikedGifts.any { it.id.value == "legends_of_chivalry" })
        assertTrue(hubert.favoriteTeas.any { it.id.value == "dagda_fruit_blend" })
        assertTrue(hubert.teaTopics.any { it.id.value == "a_new_gambit" })
        assertEquals(9, hubert.teaQuestions.size)
    }

    @Test
    fun `Hubert has no crest and Byleth M has the Flames crest`() {
        val hubert = repository.getCharacter(CharacterId("hubert"))!!
        val byleth = repository.getCharacter(CharacterId("byleth_m"))!!

        assertTrue(hubert.crests.isEmpty())
        assertTrue(byleth.crests.any { it.id.value == "flames" })
    }

    @Test
    fun `getCharacter returns null for an unknown id`() {
        assertNull(repository.getCharacter(CharacterId("not_a_real_character")))
    }

    @Test
    fun `getCharacters returns all 41 characters with no duplicate ids`() {
        val characters = repository.getCharacters()

        assertEquals(41, characters.size)
        assertEquals(characters.size, characters.map { it.id }.toSet().size)
    }

    @Test
    fun `getItems getCrests and getTeas load the full reference data`() {
        assertEquals(158, repository.getItems().size)
        assertEquals(25, repository.getCrests().size)
        assertEquals(18, repository.getTeas().size)
    }

    @Test
    fun `every character stat table is populated for all nine stat types`() {
        repository.getCharacters().forEach { character ->
            assertEquals(9, character.stats.base.size)
            assertEquals(9, character.stats.growthRates.size)
            assertEquals(9, character.stats.maximum.size)
        }
    }

    @Test
    fun `every character has all eleven proficiency types set`() {
        repository.getCharacters().forEach { character ->
            assertEquals(11, character.proficiencies.size)
        }
    }
}
