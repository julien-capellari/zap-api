package net.capellari.zap.bug_comments

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.capellari.zap.bug_comments.dtos.BugCommentResponseDto
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import java.util.Date
import java.util.UUID
import kotlin.test.assertEquals

class BugCommentServiceTest {
    private val bugCommentRepository: BugCommentRepository = mockk()
    private val bugCommentService = BugCommentService(bugCommentRepository)

    @Test
    fun `listBugComments should return all bug comments mapped to response dto`() {
        val bugId = UUID.randomUUID()

        val idA = UUID.randomUUID()
        val idB = UUID.randomUUID()

        val commentA = BugComment(bugId, idA, Date(), "toto", "life is 42")
        val commentB = BugComment(bugId, idB, Date(), "toto", "life is 42")

        every { bugCommentRepository.findByBugId(bugId) } returns listOf(commentA, commentB)

        assertEquals(
            listOf(
                BugCommentResponseDto(bugId, idA, commentA.date, commentA.username, commentA.content),
                BugCommentResponseDto(bugId, idB, commentB.date, commentB.username, commentB.content),
            ),
            bugCommentService.listBugComments(bugId)
        )

        verify(exactly = 1) { bugCommentRepository.findByBugId(bugId) }
    }

    @Test
    fun `getBugComment should return bug comment mapped to response dto`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()

        val comment = BugComment(bugId, id, Date(), "toto", "life is 42")

        every { bugCommentRepository.findByIdOrNull(BugCommentPk(bugId, id)) } returns comment

        assertEquals(
            BugCommentResponseDto(bugId, id, comment.date, comment.username, comment.content),
            bugCommentService.getBugComment(bugId, id)
        )

        verify(exactly = 1) { bugCommentRepository.findByIdOrNull(BugCommentPk(bugId, id)) }
    }
}
