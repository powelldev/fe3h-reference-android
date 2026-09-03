package com.mrp.fe3hreference.data.model

enum class GiftReaction {
    LIKED,
    DISLIKED,
}

data class Gift(
    val item: Item,
    val reaction: GiftReaction,
)
