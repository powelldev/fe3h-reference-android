package com.mrp.fe3hreference.feature.characterdetail.components

import com.mrp.fe3hreference.data.model.TeaQuestion
import com.mrp.fe3hreference.data.model.TeaTopic
import com.mrp.fe3hreference.data.model.TeaTopicId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TeasTabTest {
    private val topics =
        listOf(
            TeaTopic(id = TeaTopicId("knights"), topic = "Knights"),
            TeaTopic(id = TeaTopicId("cats"), topic = "Cats"),
        )

    private val questions =
        listOf(
            TeaQuestion(comment = "Do you have any thoughts on knights?", validAnswers = listOf("Nod")),
            TeaQuestion(comment = "What's your favorite food?", validAnswers = listOf("Sigh", "Chat")),
        )

    @Test
    fun `blank query returns every topic`() {
        assertEquals(topics, filterTeaTopics(topics, ""))
    }

    @Test
    fun `topic query is case insensitive substring match`() {
        val result = filterTeaTopics(topics, "KNIGHT")

        assertEquals(listOf(topics[0]), result)
    }

    @Test
    fun `topic query with no matches returns an empty list`() {
        assertTrue(filterTeaTopics(topics, "dragons").isEmpty())
    }

    @Test
    fun `blank query returns every question`() {
        assertEquals(questions, filterTeaQuestions(questions, ""))
    }

    @Test
    fun `question query matches the comment text`() {
        val result = filterTeaQuestions(questions, "knights")

        assertEquals(listOf(questions[0]), result)
    }

    @Test
    fun `question query matches a valid answer`() {
        val result = filterTeaQuestions(questions, "sigh")

        assertEquals(listOf(questions[1]), result)
    }

    @Test
    fun `question query with no matches returns an empty list`() {
        assertTrue(filterTeaQuestions(questions, "dragons").isEmpty())
    }
}
