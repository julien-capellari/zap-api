package net.capellari.zap.bugcomments

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import net.capellari.zap.bugcomments.dtos.BugCommentRequestDto
import net.capellari.zap.bugcomments.dtos.BugCommentResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import kotlin.test.Test

@WebMvcTest(controllers = [BugCommentController::class])
class BugCommentControllerTest(
    @Autowired val mockMvc: MockMvc
) {
    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")

    @MockkBean
    lateinit var bugCommentService: BugCommentService

    @Test
    fun `GET _bugs_{bugId}_comments should return all bug comments`() {
        val bugId = UUID.randomUUID()

        val commentA = BugCommentResponseDto(bugId, UUID.randomUUID(), Date(), "toto", "life is 42")
        val commentB = BugCommentResponseDto(bugId, UUID.randomUUID(), Date(), "toto", "life is 42")

        every { bugCommentService.listBugComments(bugId) } returns listOf(commentA, commentB)

        mockMvc.perform(get("/bugs/$bugId/comments"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(commentA.id.toString()))
            .andExpect(jsonPath("$[1].id").value(commentB.id.toString()))

        verify(exactly = 1) { bugCommentService.listBugComments(bugId) }
    }

    @Test
    fun `POST _bugs_{bugId}_comments should create a new bug comment using body data`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()

        val comment = BugCommentResponseDto(bugId, id, sdf.parse("2024-08-06T17:10:00.000Z"), "toto", "life is 42")

        every { bugCommentService.createBugComment(bugId, any()) } returns comment

        mockMvc
            .perform(
                post("/bugs/$bugId/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"date\": \"2024-08-06T17:10:00.000Z\", \"username\": \"toto\", \"content\": \"life is 42\" }")
            )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(id.toString()))

        verify(exactly = 1) {
            bugCommentService.createBugComment(
                bugId,
                BugCommentRequestDto(sdf.parse("2024-08-06T17:10:00.000Z"), "toto", "life is 42")
            )
        }
    }

    @Test
    fun `POST _bugs_{bugId}_comments should return bad request for empty json`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()

        val comment = BugCommentResponseDto(bugId, id, sdf.parse("2024-08-06T17:10:00.000Z"), "toto", "life is 42")

        every { bugCommentService.createBugComment(bugId, any()) } returns comment

        mockMvc
            .perform(
                post("/bugs/$bugId/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            )
            .andExpect(status().isBadRequest)

        verify {
            bugCommentService.createBugComment(any(), any()) wasNot Called
        }
    }

    @Test
    fun `GET _bugs_{bugId}_comments_{id} should return requested bug comment`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()

        val comment = BugCommentResponseDto(bugId, id, sdf.parse("2024-08-06T17:10:00.000Z"), "toto", "life is 42")

        every { bugCommentService.getBugComment(bugId, id) } returns comment

        mockMvc.perform(get("/bugs/$bugId/comments/$id"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.bugId").value(bugId.toString()))
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.date").value("2024-08-06T17:10:00.000+00:00"))
            .andExpect(jsonPath("$.username").value("toto"))
            .andExpect(jsonPath("$.content").value("life is 42"))

        verify(exactly = 1) { bugCommentService.getBugComment(bugId, id) }
    }

    @Test
    fun `GET _bugs_{bugId}_comments_{id} should return not found`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()

        every { bugCommentService.getBugComment(any(), any()) } returns null

        mockMvc.perform(get("/bugs/$bugId/comments/$id"))
            .andExpect(status().isNotFound)

        verify(exactly = 1) { bugCommentService.getBugComment(bugId, id) }
    }

    @Test
    fun `PUT _bugs_{bugId}_comments_{id} should update bug comment using body data`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()

        val comment = BugCommentResponseDto(bugId, id, sdf.parse("2024-08-06T17:10:00.000Z"), "toto", "life is 42")

        every { bugCommentService.updateBugComment(any(), any(), any()) } returns comment

        mockMvc
            .perform(
                put("/bugs/$bugId/comments/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"date\": \"2024-08-06T17:10:00.000Z\", \"username\": \"toto\", \"content\": \"updated\" }")
            )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(id.toString()))

        verify(exactly = 1) {
            bugCommentService.updateBugComment(
                bugId,
                id,
                BugCommentRequestDto(sdf.parse("2024-08-06T17:10:00.000Z"), "toto", "updated")
            )
        }
    }

    @Test
    fun `PUT _bugs_{bugId}_comments_{id} should return not found if bug doesn't exist`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()

        every { bugCommentService.updateBugComment(any(), any(), any()) } returns null

        mockMvc
            .perform(
                put("/bugs/$bugId/comments/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"date\": \"2024-08-06T17:10:00.000Z\", \"username\": \"toto\", \"content\": \"updated\" }")
            )
            .andExpect(status().isNotFound)

        verify(exactly = 1) { bugCommentService.updateBugComment(bugId, id, any()) }
    }

    @Test
    fun `PUT _bugs_{bugId}_comments_{id} should return bad request for empty json`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()

        every { bugCommentService.updateBugComment(any(), any(), any()) } returns null

        mockMvc
            .perform(
                put("/bugs/$bugId/comments/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            )
            .andExpect(status().isBadRequest)

        verify(exactly = 1) {
            bugCommentService.updateBugComment(any(), any(), any())!! wasNot Called
        }
    }

    @Test
    fun `DELETE _bugs_{bugId}_comments_{id} should delete requested bug comment`() {
        val bugId = UUID.randomUUID()
        val id = UUID.randomUUID()

        every { bugCommentService.deleteBugComment(any(), any()) } just Runs

        mockMvc.perform(delete("/bugs/$bugId/comments/$id"))
            .andExpect(status().isNoContent)

        verify(exactly = 1) { bugCommentService.deleteBugComment(bugId, id) }
    }
}
