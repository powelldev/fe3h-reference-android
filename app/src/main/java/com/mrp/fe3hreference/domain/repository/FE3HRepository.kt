package com.mrp.fe3hreference.domain.repository

import com.mrp.fe3hreference.data.model.Character
import com.mrp.fe3hreference.data.model.CharacterId
import com.mrp.fe3hreference.data.model.Crest
import com.mrp.fe3hreference.data.model.Item
import com.mrp.fe3hreference.data.model.Tea

interface FE3HRepository {
    fun getCharacters(): List<Character>

    fun getCharacter(id: CharacterId): Character?

    fun getItems(): List<Item>

    fun getCrests(): List<Crest>

    fun getTeas(): List<Tea>
}
