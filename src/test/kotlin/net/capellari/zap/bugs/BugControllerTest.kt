package net.capellari.zap.bugs

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import net.capellari.zap.bugs.dtos.BugRequestDto
import net.capellari.zap.bugs.dtos.BugResponseDto
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

@WebMvcTest(BugController::class)
class BugControllerTest(
    @Autowired val mockMvc: MockMvc
) {
    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")

    @MockkBean
    lateinit var bugService: BugService

    @Test
    fun `GET _bugs should return all bugs`() {
        val bugA = BugResponseDto(UUID.randomUUID(), "Test", Date(), 1, BugStatus.TODO, "")
        val bugB = BugResponseDto(UUID.randomUUID(), "Test", Date(), 1, BugStatus.TODO, "")

        every { bugService.listBugs() } returns listOf(bugA, bugB)

        mockMvc.perform(get("/bugs"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(bugA.id.toString()))
            .andExpect(jsonPath("$[1].id").value(bugB.id.toString()))

        verify(exactly = 1) { bugService.listBugs() }
    }

    @Test
    fun `POST _bugs should create a new bug using body data`() {
        val id = UUID.randomUUID()
        val bug =
            BugResponseDto(id, "Test", sdf.parse("2024-08-06T17:10:00.000Z"), 2, BugStatus.VALIDATED, "life is 42")

        every { bugService.createBug(any()) } returns bug

        mockMvc
            .perform(
                post("/bugs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"title\": \"Test\", \"date\": \"2024-08-06T17:10:00.000Z\", \"severity\": 2, \"status\": \"VALIDATED\", \"description\": \"life is 42\" }")
            )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(id.toString()))

        verify(exactly = 1) {
            bugService.createBug(
                BugRequestDto("Test", sdf.parse("2024-08-06T17:10:00.000Z"), 2, BugStatus.VALIDATED, "life is 42")
            )
        }
    }

    @Test
    fun `POST _bugs should create a new bug with default values`() {
        val id = UUID.randomUUID()
        val bug = BugResponseDto(id, "Test", sdf.parse("2024-08-06T17:10:00.000Z"), 1, BugStatus.TODO, "")

        every { bugService.createBug(any()) } returns bug

        mockMvc
            .perform(
                post("/bugs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"title\": \"Test\", \"date\": \"2024-08-06T17:10:00.000Z\", \"severity\": 1 }")
            )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(id.toString()))

        verify(exactly = 1) {
            bugService.createBug(
                BugRequestDto("Test", sdf.parse("2024-08-06T17:10:00.000Z"), 1)
            )
        }
    }

    @Test
    fun `POST _bugs should return bad request for empty json`() {
        val id = UUID.randomUUID()
        val bug = BugResponseDto(id, "Test", sdf.parse("2024-08-06T17:10:00.000Z"), 1, BugStatus.TODO, "")

        every { bugService.createBug(any()) } returns bug

        mockMvc
            .perform(
                post("/bugs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            )
            .andExpect(status().isBadRequest)

        verify {
            bugService.createBug(any()) wasNot Called
        }
    }

    @Test
    fun `GET _bugs_{id} should return requested bug`() {
        val id = UUID.randomUUID()
        val bug = BugResponseDto(id, "Test", sdf.parse("2024-08-06T16:48:00.000Z"), 1, BugStatus.TODO, "")

        every { bugService.getBug(any()) } returns bug

        mockMvc.perform(get("/bugs/$id"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.title").value("Test"))
            .andExpect(jsonPath("$.date").value("2024-08-06T16:48:00.000+00:00"))
            .andExpect(jsonPath("$.severity").value("1"))
            .andExpect(jsonPath("$.status").value("TODO"))
            .andExpect(jsonPath("$.description").value(""))

        verify(exactly = 1) { bugService.getBug(id) }
    }

    @Test
    fun `GET _bugs_{id} should return not found`() {
        val id = UUID.randomUUID()

        every { bugService.getBug(any()) } returns null

        mockMvc.perform(get("/bugs/$id"))
            .andExpect(status().isNotFound)

        verify(exactly = 1) { bugService.getBug(id) }
    }

    @Test
    fun `PUT _bugs_{id} should update bug using body data`() {
        val id = UUID.randomUUID()
        val bug =
            BugResponseDto(id, "Test", sdf.parse("2024-08-06T17:10:00.000Z"), 2, BugStatus.VALIDATED, "life is 42")

        every { bugService.updateBug(any(), any()) } returns bug

        mockMvc
            .perform(
                put("/bugs/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"title\": \"Test\", \"date\": \"2024-08-06T17:10:00.000Z\", \"severity\": 2, \"status\": \"VALIDATED\", \"description\": \"life is 42\" }")
            )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(id.toString()))

        verify(exactly = 1) {
            bugService.updateBug(
                id,
                BugRequestDto("Test", sdf.parse("2024-08-06T17:10:00.000Z"), 2, BugStatus.VALIDATED, "life is 42")
            )
        }
    }

    @Test
    fun `PUT _bugs_{id} should update bug with default values`() {
        val id = UUID.randomUUID()
        val bug = BugResponseDto(id, "Test", sdf.parse("2024-08-06T17:10:00.000Z"), 1, BugStatus.TODO, "")

        every { bugService.updateBug(any(), any()) } returns bug

        mockMvc
            .perform(
                put("/bugs/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"title\": \"Test\", \"date\": \"2024-08-06T17:10:00.000Z\", \"severity\": 1 }")
            )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(id.toString()))

        verify(exactly = 1) {
            bugService.updateBug(
                id,
                BugRequestDto("Test", sdf.parse("2024-08-06T17:10:00.000Z"), 1)
            )
        }
    }

    @Test
    fun `PUT _bugs_{id} should return not found if bug doesn't exist`() {
        val id = UUID.randomUUID()

        every { bugService.updateBug(any(), any()) } returns null

        mockMvc
            .perform(
                put("/bugs/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{ \"title\": \"Test\", \"date\": \"2024-08-06T17:10:00.000Z\", \"severity\": 2, \"status\": \"VALIDATED\", \"description\": \"life is 42\" }")
            )
            .andExpect(status().isNotFound)

        verify(exactly = 1) { bugService.updateBug(id, any()) }
    }

    @Test
    fun `PUT _bugs_{id} should return bad request for empty json`() {
        val id = UUID.randomUUID()

        every { bugService.updateBug(any(), any()) } returns null

        mockMvc
            .perform(
                put("/bugs/$id")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}")
            )
            .andExpect(status().isBadRequest)

        verify(exactly = 1) {
            bugService.updateBug(id, any())?.wasNot(Called)
        }
    }

    @Test
    fun `DELETE _bugs_{id} should delete requested bug`() {
        val id = UUID.randomUUID()

        every { bugService.deleteBug(any()) } just Runs

        mockMvc.perform(delete("/bugs/$id"))
            .andExpect(status().isNoContent)

        verify(exactly = 1) { bugService.deleteBug(id) }
    }
}
