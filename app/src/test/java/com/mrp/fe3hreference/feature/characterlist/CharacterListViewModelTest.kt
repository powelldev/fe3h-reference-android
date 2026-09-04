package com.mrp.fe3hreference.feature.characterlist

import com.mrp.fe3hreference.data.model.Character
import com.mrp.fe3hreference.data.model.CharacterId
import com.mrp.fe3hreference.data.repository.FE3HRepositoryImpl
import com.mrp.fe3hreference.data.source.FE3HJsonParser
import com.mrp.fe3hreference.data.source.TestFE3HRawDataSource
import com.mrp.fe3hreference.domain.repository.FE3HRepository
import com.mrp.fe3hreference.testutil.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CharacterListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: FE3HRepository =
        FE3HRepositoryImpl(TestFE3HRawDataSource(), FE3HJsonParser(), ioDispatcher = Dispatchers.Unconfined)

    @Test
    fun `initial state is Success with all characters once loaded`() =
        runTest {
            val viewModel = CharacterListViewModel(repository)

            val state = viewModel.uiState.value

            assertTrue(state is CharacterListUiState.Success)
            assertEquals(41, (state as CharacterListUiState.Success).characters.size)
        }

    @Test
    fun `loaded characters include Hubert`() =
        runTest {
            val viewModel = CharacterListViewModel(repository)

            val state = viewModel.uiState.value as CharacterListUiState.Success

            assertTrue(state.characters.any { it.id == CharacterId("hubert") })
        }

    @Test
    fun `repository failure surfaces as Error state`() =
        runTest {
            val failingRepository =
                object : FE3HRepository by repository {
                    override suspend fun getCharacters(): List<Character> = error("data unavailable")
                }

            val viewModel = CharacterListViewModel(failingRepository)

            val state = viewModel.uiState.value

            assertTrue(state is CharacterListUiState.Error)
            assertEquals("data unavailable", (state as CharacterListUiState.Error).message)
        }
}
