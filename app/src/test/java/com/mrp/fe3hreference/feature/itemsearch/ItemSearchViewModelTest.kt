package com.mrp.fe3hreference.feature.itemsearch

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

class ItemSearchViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: FE3HRepository =
        FE3HRepositoryImpl(TestFE3HRawDataSource(), FE3HJsonParser(), ioDispatcher = Dispatchers.Unconfined)

    @Test
    fun `initial state is an empty query with no results`() =
        runTest {
            val viewModel = ItemSearchViewModel(repository)

            val state = viewModel.uiState.value

            assertEquals("", state.query)
            assertTrue(state.results.isEmpty())
        }

    @Test
    fun `typing part of an item name finds its owner`() =
        runTest {
            val viewModel = ItemSearchViewModel(repository)

            viewModel.onQueryChanged("white glove")

            val results = viewModel.uiState.value.results
            assertTrue(results.any { it.item.id.value == "white_glove" && it.owner.id == CharacterId("edelgard") })
        }

    @Test
    fun `search is case insensitive`() =
        runTest {
            val viewModel = ItemSearchViewModel(repository)

            viewModel.onQueryChanged("WHITE GLOVE")

            assertTrue(
                viewModel.uiState.value.results
                    .any { it.item.id.value == "white_glove" },
            )
        }

    @Test
    fun `blank query returns no results`() =
        runTest {
            val viewModel = ItemSearchViewModel(repository)

            viewModel.onQueryChanged("white glove")
            viewModel.onQueryChanged("")

            assertTrue(
                viewModel.uiState.value.results
                    .isEmpty(),
            )
        }

    @Test
    fun `query with no matches returns no results`() =
        runTest {
            val viewModel = ItemSearchViewModel(repository)

            viewModel.onQueryChanged("xyzzy_not_a_real_item")

            assertTrue(
                viewModel.uiState.value.results
                    .isEmpty(),
            )
        }
}
