package com.mrp.fe3hreference.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TeasFileDto(
    val teas: List<TeaDto>,
    val topics: List<TeaTopicDto>,
)

@Serializable
data class TeaDto(
    val id: String,
    val name: String,
)

@Serializable
data class TeaTopicDto(
    val id: String,
    val topic: String,
)
