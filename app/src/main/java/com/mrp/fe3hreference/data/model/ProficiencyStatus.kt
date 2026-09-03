package com.mrp.fe3hreference.data.model

sealed interface ProficiencyStatus {
    data object Neutral : ProficiencyStatus

    data object Boon : ProficiencyStatus

    data object Bane : ProficiencyStatus

    data object BuddingTalent : ProficiencyStatus
}
