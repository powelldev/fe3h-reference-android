package com.mrp.fe3hreference.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemsFileDto(
    val items: List<ItemDto>,
)

@Serializable
data class ItemDto(
    val id: String,
    val name: String,
)
