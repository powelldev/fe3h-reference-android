package com.mrp.fe3hreference.feature.characterdetail.components

import androidx.compose.ui.graphics.Color
import com.mrp.fe3hreference.data.model.ProficiencyStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class ProficiencyTableTest {
    @Test
    fun `neutral has no symbol`() {
        assertEquals("", proficiencySymbol(ProficiencyStatus.Neutral))
    }

    @Test
    fun `boon shows an up arrow`() {
        assertEquals("▲", proficiencySymbol(ProficiencyStatus.Boon))
    }

    @Test
    fun `bane shows a down arrow`() {
        assertEquals("▼", proficiencySymbol(ProficiencyStatus.Bane))
    }

    @Test
    fun `budding talent shows three stars`() {
        assertEquals("★★★", proficiencySymbol(ProficiencyStatus.BuddingTalent))
    }

    @Test
    fun `boon and bane are colored distinctly`() {
        assertEquals(Color(0xFF1565C0), proficiencyColor(ProficiencyStatus.Boon))
        assertEquals(Color(0xFFC62828), proficiencyColor(ProficiencyStatus.Bane))
    }
}
