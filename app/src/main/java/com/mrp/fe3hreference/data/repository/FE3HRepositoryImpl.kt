package com.mrp.fe3hreference.data.repository

import com.mrp.fe3hreference.data.dto.TeasFileDto
import com.mrp.fe3hreference.data.mapper.toDomain
import com.mrp.fe3hreference.data.model.Character
import com.mrp.fe3hreference.data.model.CharacterId
import com.mrp.fe3hreference.data.model.Crest
import com.mrp.fe3hreference.data.model.CrestId
import com.mrp.fe3hreference.data.model.Item
import com.mrp.fe3hreference.data.model.ItemId
import com.mrp.fe3hreference.data.model.Tea
import com.mrp.fe3hreference.data.model.TeaId
import com.mrp.fe3hreference.data.model.TeaTopic
import com.mrp.fe3hreference.data.model.TeaTopicId
import com.mrp.fe3hreference.data.source.FE3HJsonParser
import com.mrp.fe3hreference.data.source.FE3HRawDataSource
import com.mrp.fe3hreference.domain.repository.FE3HRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FE3HRepositoryImpl(
    private val rawDataSource: FE3HRawDataSource,
    private val parser: FE3HJsonParser = FE3HJsonParser(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FE3HRepository {
    private val allItems: List<Item> by lazy {
        parser.parseItems(rawDataSource.itemsJson()).map { it.toDomain() }
    }

    private val itemsById: Map<ItemId, Item> by lazy { allItems.associateBy { it.id } }

    private val allCrests: List<Crest> by lazy {
        parser.parseCrests(rawDataSource.crestsJson()).map { it.toDomain() }
    }

    private val crestsById: Map<CrestId, Crest> by lazy { allCrests.associateBy { it.id } }

    private val teasFile: TeasFileDto by lazy { parser.parseTeas(rawDataSource.teasJson()) }

    private val allTeas: List<Tea> by lazy { teasFile.teas.map { it.toDomain() } }

    private val teasById: Map<TeaId, Tea> by lazy { allTeas.associateBy { it.id } }

    private val teaTopicsById: Map<TeaTopicId, TeaTopic> by lazy {
        teasFile.topics.map { it.toDomain() }.associateBy { it.id }
    }

    private val allCharacters: List<Character> by lazy {
        parser.parseCharacters(rawDataSource.charactersJson()).map { dto ->
            dto.toDomain(itemsById, crestsById, teasById, teaTopicsById)
        }
    }

    private val charactersById: Map<CharacterId, Character> by lazy { allCharacters.associateBy { it.id } }

    override suspend fun getCharacters(): List<Character> = withContext(ioDispatcher) { allCharacters }

    override suspend fun getCharacter(id: CharacterId): Character? = withContext(ioDispatcher) { charactersById[id] }

    override suspend fun getItems(): List<Item> = withContext(ioDispatcher) { allItems }

    override suspend fun getCrests(): List<Crest> = withContext(ioDispatcher) { allCrests }

    override suspend fun getTeas(): List<Tea> = withContext(ioDispatcher) { allTeas }
}
