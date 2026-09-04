package com.mrp.fe3hreference.data.source

import org.junit.Assert.assertEquals
import org.junit.Test

class FE3HJsonParserTest {
    private val parser = FE3HJsonParser()

    @Test
    fun `parseCharacters reads character fields including nested stats and tea questions`() {
        val raw =
            """
            {
              "characters": [
                {
                  "id": "hubert",
                  "name": "Hubert",
                  "portrait": "hubert",
                  "majorCrestId": null,
                  "minorCrestId": null,
                  "proficiencies": { "LANCE": "BUDDING_TALENT" },
                  "stats": {
                    "base": { "HP": 22 },
                    "growthRates": { "HP": 35 },
                    "maximum": { "HP": 67 }
                  },
                  "lostItemIds": ["hresvelg_treatise"],
                  "likedGiftIds": ["coffee_beans"],
                  "dislikedGiftIds": ["legends_of_chivalry"],
                  "favoriteTeaIds": ["dagda_fruit_blend"],
                  "teaTopicIds": ["a_new_gambit"],
                  "teaQuestions": [
                    { "comment": "A question.", "validAnswers": ["Nod", "Sip tea"] }
                  ]
                }
              ]
            }
            """.trimIndent()

        val characters = parser.parseCharacters(raw)

        assertEquals(1, characters.size)
        val hubert = characters.single()
        assertEquals("hubert", hubert.id)
        assertEquals("Hubert", hubert.name)
        assertEquals(mapOf("LANCE" to "BUDDING_TALENT"), hubert.proficiencies)
        assertEquals(22, hubert.stats.base["HP"])
        assertEquals(listOf("hresvelg_treatise"), hubert.lostItemIds)
        assertEquals(1, hubert.teaQuestions.size)
        assertEquals(listOf("Nod", "Sip tea"), hubert.teaQuestions.single().validAnswers)
    }

    @Test
    fun `parseItems reads the items list`() {
        val raw = """{ "items": [ { "id": "coffee_beans", "name": "Coffee Beans" } ] }"""

        val items = parser.parseItems(raw)

        assertEquals(1, items.size)
        assertEquals("coffee_beans", items.single().id)
        assertEquals("Coffee Beans", items.single().name)
    }

    @Test
    fun `parseCrests reads the crests list`() {
        val raw =
            """{ "crests": [ { "id": "flames", "name": "Flames", "description": "The Crest of the goddess." } ] }"""

        val crests = parser.parseCrests(raw)

        assertEquals(1, crests.size)
        assertEquals("flames", crests.single().id)
        assertEquals("The Crest of the goddess.", crests.single().description)
    }

    @Test
    fun `parseTeas reads teas and topics`() {
        val raw =
            """
            {
              "teas": [ { "id": "bergamot", "name": "Bergamot" } ],
              "topics": [ { "id": "a_new_gambit", "topic": "A new gambit" } ]
            }
            """.trimIndent()

        val result = parser.parseTeas(raw)

        assertEquals(1, result.teas.size)
        assertEquals("bergamot", result.teas.single().id)
        assertEquals(1, result.topics.size)
        assertEquals("a_new_gambit", result.topics.single().id)
    }
}
