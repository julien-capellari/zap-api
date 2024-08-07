package net.capellari.zap.bugcomments

import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import net.capellari.zap.bugcomments.dtos.BugCommentRequestDto
import net.capellari.zap.bugcomments.dtos.BugCommentResponseDto
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import java.util.Date
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

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

    @Test
    fun `getBugComment should return null if requested bug comment does not exists`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()

        every { bugCommentRepository.findByIdOrNull(BugCommentPk(bugId, id)) } returns null

        assertNull(bugCommentService.getBugComment(bugId, id))

        verify(exactly = 1) { bugCommentRepository.findByIdOrNull(BugCommentPk(bugId, id)) }
    }

    @Test
    fun `createBugComment should create bug comment and return it mapped to response dto`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()
        val date = Date()

        val payload = BugCommentRequestDto(date, "toto", "updated")

        every { bugCommentRepository.save(any()) } returns BugComment(bugId, id, date, "toto", "updated")

        assertEquals(
            BugCommentResponseDto(bugId, id, date, "toto", "updated"),
            bugCommentService.createBugComment(bugId, payload)
        )

        verify(exactly = 1) { bugCommentRepository.save(BugComment(bugId, null, date, "toto", "updated")) }
    }

    @Test
    fun `updateBugComment should create bug comment and return it mapped to response dto`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()
        val date = Date()

        val payload = BugCommentRequestDto(date, "toto", "updated")

        every { bugCommentRepository.findByIdOrNull(BugCommentPk(bugId, id)) } returns BugComment(bugId, id, Date(), "toto", "life is 42")
        every { bugCommentRepository.save(any()) } returnsArgument 0

        assertEquals(
            BugCommentResponseDto(bugId, id, date, "toto", "updated"),
            bugCommentService.updateBugComment(bugId, id, payload)
        )

        verify(exactly = 1) {
            bugCommentRepository.findByIdOrNull(BugCommentPk(bugId, id))
            bugCommentRepository.save(BugComment(bugId, id, date, "toto", "updated"))
        }
    }

    @Test
    fun `updateBugComment should return null if updated bug comment does not exists`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()
        val date = Date()

        val payload = BugCommentRequestDto(date, "toto", "updated")

        every { bugCommentRepository.findByIdOrNull(BugCommentPk(bugId, id)) } returns null
        every { bugCommentRepository.save(any()) } returnsArgument 0

        assertNull(bugCommentService.updateBugComment(bugId, id, payload))

        verify(exactly = 1) {
            bugCommentRepository.findByIdOrNull(BugCommentPk(bugId, id))
            bugCommentRepository.save(any()) wasNot Called
        }
    }

    @Test
    fun `deleteBugComment should return delete requested bug comment`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()

        every { bugCommentRepository.deleteById(BugCommentPk(bugId, id)) } just Runs

        bugCommentService.deleteBugComment(bugId, id)

        verify(exactly = 1) { bugCommentRepository.deleteById(BugCommentPk(bugId, id)) }
    }
}
