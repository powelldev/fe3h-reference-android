package com.mrp.fe3hreference.data.model

data class CharacterStats(
    val base: Map<StatType, Int>,
    val growthRates: Map<StatType, Int>,
    val maximum: Map<StatType, Int>,
)
