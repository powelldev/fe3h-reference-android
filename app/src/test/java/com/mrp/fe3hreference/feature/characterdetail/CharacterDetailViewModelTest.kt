package com.mrp.fe3hreference.feature.characterdetail

import com.mrp.fe3hreference.data.model.CharacterId
import com.mrp.fe3hreference.data.model.ProficiencyStatus
import com.mrp.fe3hreference.data.model.ProficiencyType
import com.mrp.fe3hreference.data.repository.FE3HRepositoryImpl
import com.mrp.fe3hreference.data.source.FE3HJsonParser
import com.mrp.fe3hreference.data.source.TestFE3HRawDataSource
import com.mrp.fe3hreference.domain.repository.FE3HRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterDetailViewModelTest {
    private val repository: FE3HRepository = FE3HRepositoryImpl(TestFE3HRawDataSource(), FE3HJsonParser())

    @Test
    fun `loads Hubert into Success state with Stats tab selected by default`() {
        val viewModel = CharacterDetailViewModel(CharacterId("hubert"), repository)

        val state = viewModel.uiState.value

        assertTrue(state is CharacterDetailUiState.Success)
        val success = state as CharacterDetailUiState.Success
        assertEquals("Hubert", success.character.name)
        assertEquals(CharacterDetailTab.STATS, success.selectedTab)
    }

    @Test
    fun `Hubert's Stats tab data matches his proficiencies`() {
        val viewModel = CharacterDetailViewModel(CharacterId("hubert"), repository)

        val success = viewModel.uiState.value as CharacterDetailUiState.Success

        assertEquals(ProficiencyStatus.BuddingTalent, success.character.proficiencies[ProficiencyType.LANCE])
        assertEquals(ProficiencyStatus.Bane, success.character.proficiencies[ProficiencyType.AXE])
    }

    @Test
    fun `onTabSelected switches the selected tab`() {
        val viewModel = CharacterDetailViewModel(CharacterId("hubert"), repository)

        viewModel.onTabSelected(CharacterDetailTab.TEAS)

        val success = viewModel.uiState.value as CharacterDetailUiState.Success
        assertEquals(CharacterDetailTab.TEAS, success.selectedTab)
    }

    @Test
    fun `unknown character id surfaces as Error state`() {
        val viewModel = CharacterDetailViewModel(CharacterId("not_a_real_character"), repository)

        val state = viewModel.uiState.value

        assertTrue(state is CharacterDetailUiState.Error)
    }
}
