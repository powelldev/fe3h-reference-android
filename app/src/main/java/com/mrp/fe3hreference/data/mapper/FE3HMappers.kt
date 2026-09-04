package com.mrp.fe3hreference.data.mapper

import com.mrp.fe3hreference.data.dto.CharacterDto
import com.mrp.fe3hreference.data.dto.CharacterStatsDto
import com.mrp.fe3hreference.data.dto.CrestDto
import com.mrp.fe3hreference.data.dto.ItemDto
import com.mrp.fe3hreference.data.dto.TeaDto
import com.mrp.fe3hreference.data.dto.TeaQuestionDto
import com.mrp.fe3hreference.data.dto.TeaTopicDto
import com.mrp.fe3hreference.data.model.Character
import com.mrp.fe3hreference.data.model.CharacterId
import com.mrp.fe3hreference.data.model.CharacterStats
import com.mrp.fe3hreference.data.model.Crest
import com.mrp.fe3hreference.data.model.CrestId
import com.mrp.fe3hreference.data.model.Item
import com.mrp.fe3hreference.data.model.ItemId
import com.mrp.fe3hreference.data.model.ProficiencyStatus
import com.mrp.fe3hreference.data.model.ProficiencyType
import com.mrp.fe3hreference.data.model.StatType
import com.mrp.fe3hreference.data.model.Tea
import com.mrp.fe3hreference.data.model.TeaId
import com.mrp.fe3hreference.data.model.TeaQuestion
import com.mrp.fe3hreference.data.model.TeaTopic
import com.mrp.fe3hreference.data.model.TeaTopicId

fun ItemDto.toDomain(): Item = Item(id = ItemId(id), name = name)

fun CrestDto.toDomain(): Crest = Crest(id = CrestId(id), name = name, description = description)

fun TeaDto.toDomain(): Tea = Tea(id = TeaId(id), name = name)

fun TeaTopicDto.toDomain(): TeaTopic = TeaTopic(id = TeaTopicId(id), topic = topic)

fun TeaQuestionDto.toDomain(): TeaQuestion = TeaQuestion(comment = comment, validAnswers = validAnswers)

fun CharacterStatsDto.toDomain(): CharacterStats =
    CharacterStats(
        base = base.toStatMap(),
        growthRates = growthRates.toStatMap(),
        maximum = maximum.toStatMap(),
    )

private fun Map<String, Int>.toStatMap(): Map<StatType, Int> =
    entries.associate { (key, value) -> StatType.valueOf(key) to value }

fun parseProficiencyStatus(raw: String): ProficiencyStatus =
    when (raw) {
        "NEUTRAL" -> ProficiencyStatus.Neutral
        "BOON" -> ProficiencyStatus.Boon
        "BANE" -> ProficiencyStatus.Bane
        "BUDDING_TALENT" -> ProficiencyStatus.BuddingTalent
        else -> error("Unknown proficiency status: $raw")
    }

fun CharacterDto.toDomain(
    itemsById: Map<ItemId, Item>,
    crestsById: Map<CrestId, Crest>,
    teasById: Map<TeaId, Tea>,
    teaTopicsById: Map<TeaTopicId, TeaTopic>,
): Character {
    fun resolveItem(itemId: String): Item =
        itemsById[ItemId(itemId)] ?: error("Character '$id' references unknown item id '$itemId'")

    fun resolveCrest(crestId: String): Crest =
        crestsById[CrestId(crestId)] ?: error("Character '$id' references unknown crest id '$crestId'")

    fun resolveTea(teaId: String): Tea =
        teasById[TeaId(teaId)] ?: error("Character '$id' references unknown tea id '$teaId'")

    fun resolveTeaTopic(topicId: String): TeaTopic =
        teaTopicsById[TeaTopicId(topicId)] ?: error("Character '$id' references unknown tea topic id '$topicId'")

    return Character(
        id = CharacterId(id),
        name = name,
        portrait = portrait,
        crests = listOfNotNull(majorCrestId, minorCrestId).map(::resolveCrest),
        proficiencies =
            proficiencies.entries.associate { (key, value) ->
                ProficiencyType.valueOf(key) to parseProficiencyStatus(value)
            },
        stats = stats.toDomain(),
        lostItems = lostItemIds.map(::resolveItem),
        likedGifts = likedGiftIds.map(::resolveItem),
        dislikedGifts = dislikedGiftIds.map(::resolveItem),
        favoriteTeas = favoriteTeaIds.map(::resolveTea),
        teaTopics = teaTopicIds.map(::resolveTeaTopic),
        teaQuestions = teaQuestions.map { it.toDomain() },
    )
}
