package com.mrp.fe3hreference.data.source

import com.mrp.fe3hreference.data.dto.CharacterDto
import com.mrp.fe3hreference.data.dto.CharactersFileDto
import com.mrp.fe3hreference.data.dto.CrestDto
import com.mrp.fe3hreference.data.dto.CrestsFileDto
import com.mrp.fe3hreference.data.dto.ItemDto
import com.mrp.fe3hreference.data.dto.ItemsFileDto
import com.mrp.fe3hreference.data.dto.TeasFileDto
import kotlinx.serialization.json.Json

class FE3HJsonParser(
    private val json: Json = Json { ignoreUnknownKeys = true },
) {
    fun parseCharacters(raw: String): List<CharacterDto> = json.decodeFromString<CharactersFileDto>(raw).characters

    fun parseItems(raw: String): List<ItemDto> = json.decodeFromString<ItemsFileDto>(raw).items

    fun parseCrests(raw: String): List<CrestDto> = json.decodeFromString<CrestsFileDto>(raw).crests

    fun parseTeas(raw: String): TeasFileDto = json.decodeFromString(raw)
}
