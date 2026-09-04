package com.mrp.fe3hreference.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CrestsFileDto(
    val crests: List<CrestDto>,
)

@Serializable
data class CrestDto(
    val id: String,
    val name: String,
    val description: String,
)
