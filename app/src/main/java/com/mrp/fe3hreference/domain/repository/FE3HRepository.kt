package com.mrp.fe3hreference.domain.repository

import com.mrp.fe3hreference.data.model.Character
import com.mrp.fe3hreference.data.model.CharacterId
import com.mrp.fe3hreference.data.model.Crest
import com.mrp.fe3hreference.data.model.Item
import com.mrp.fe3hreference.data.model.LostItem
import com.mrp.fe3hreference.data.model.Tea

interface FE3HRepository {
    suspend fun getCharacters(): List<Character>

    suspend fun getCharacter(id: CharacterId): Character?

    suspend fun getItems(): List<Item>

    suspend fun getCrests(): List<Crest>

    suspend fun getTeas(): List<Tea>

    suspend fun getLostItems(): List<LostItem>
}
