package net.capellari.zap.bugs

import io.mockk.Called
import io.mockk.Runs
import io.mockk.called
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import net.capellari.zap.EmailService
import net.capellari.zap.Order
import net.capellari.zap.bugs.dtos.BugFiltersDto
import net.capellari.zap.bugs.dtos.BugRequestDto
import net.capellari.zap.bugs.dtos.BugResponseDto
import org.springframework.data.domain.Example
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.server.ResponseStatusException
import java.util.Date
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class BugServiceTest {
    private val bugRepository: BugRepository = mockk()
    private val emailService: EmailService = mockk()

    private val bugService = BugService(bugRepository, emailService)

    @Test
    fun `listBug should return all bugs mapped to response dto`() {
        val idA = UUID.randomUUID()
        val idB = UUID.randomUUID()

        val bugA = Bug(idA, "Test", Date(), 1, BugStatus.TODO, "")
        val bugB = Bug(idB, "Test", Date(), 1, BugStatus.TODO, "")

        every { bugRepository.findAll(any<Example<Bug>>(), any<Sort>()) } returns listOf(bugA, bugB)

        assertEquals(
            listOf(
                BugResponseDto(idA, bugA.title, bugA.date, bugA.severity, bugA.status, bugA.description),
                BugResponseDto(idB, bugB.title, bugB.date, bugB.severity, bugB.status, bugB.description),
            ),
            bugService.listBugs(BugFiltersDto(null, null), Order.DESC)
        )

        verify(exactly = 1) { bugRepository.findAll(any<Example<Bug>>(), any<Sort>()) }
    }

    @Test
    fun `getBug should return bug mapped to response dto`() {
        val id = UUID.randomUUID()
        val bug = Bug(id, "Test", Date(), 1, BugStatus.TODO, "")

        every { bugRepository.findByIdOrNull(id) } returns bug

        assertEquals(
            BugResponseDto(id, bug.title, bug.date, bug.severity, bug.status, bug.description),
            bugService.getBug(id)
        )

        verify(exactly = 1) { bugRepository.findById(id) }
    }

    @Test
    fun `getBug should return null if requested bug does not exists`() {
        val id = UUID.randomUUID()

        every { bugRepository.findByIdOrNull(id) } returns null

        assertNull(bugService.getBug(id))

        verify(exactly = 1) { bugRepository.findById(id) }
    }

    @Test
    fun `createBug should create bug and return it mapped to response dto`() {
        val id = UUID.randomUUID()
        val date = Date()
        val payload = BugRequestDto("Test", date, 1, BugStatus.TODO, "")

        every { bugRepository.save(any()) } returns Bug(id, "Test", date, 1, BugStatus.TODO, "")

        assertEquals(
            BugResponseDto(id, "Test", date, 1, BugStatus.TODO, ""),
            bugService.createBug(payload)
        )

        verify(exactly = 1) { bugRepository.save(Bug(null, "Test", date, 1, BugStatus.TODO, "")) }
    }

    @Test
    fun `updateBug should create bug and return it mapped to response dto`() {
        val id = UUID.randomUUID()
        val date = Date()
        val payload = BugRequestDto("Test updated", date, 2, BugStatus.TODO, "updated")

        every { bugRepository.findByIdOrNull(id) } returns Bug(id, "Test", date, 1, BugStatus.VALIDATED, "")
        every { bugRepository.save(any()) } returnsArgument 0
        every { emailService.sendEmail(any(), any(), any()) } just Runs

        assertEquals(
            BugResponseDto(id, "Test updated", date, 2, BugStatus.TODO, "updated"),
            bugService.updateBug(id, payload)
        )

        verify(exactly = 1) {
            bugRepository.findById(id)
            bugRepository.save(Bug(id, "Test updated", date, 2, BugStatus.TODO, "updated"))
            emailService.sendEmail("admin@zap.net", "Bug status changed", "Bug Test updated changed from VALIDATED to TODO status")
        }
    }

    @Test
    fun `updateBug should forbid update from TODO to VALIDATED`() {
        val id = UUID.randomUUID()
        val date = Date()
        val payload = BugRequestDto("Test updated", date, 2, BugStatus.VALIDATED, "updated")

        every { bugRepository.findByIdOrNull(id) } returns Bug(id, "Test", date, 1, BugStatus.TODO, "")
        every { bugRepository.save(any()) } returnsArgument 0

        assertFailsWith(ResponseStatusException::class) {
            bugService.updateBug(id, payload)
        }

        verify(exactly = 1) {
            bugRepository.findById(id)
            bugRepository.save(any()) wasNot Called
        }
    }

    @Test
    fun `updateBug should return null if updated bug does not exists`() {
        val id = UUID.randomUUID()
        val date = Date()
        val payload = BugRequestDto("Test updated", date, 2, BugStatus.VALIDATED, "updated")

        every { bugRepository.findByIdOrNull(id) } returns null
        every { bugRepository.save(any()) } returnsArgument 0

        assertNull(bugService.updateBug(id, payload))

        verify(exactly = 1) {
            bugRepository.findById(id)
            bugRepository.save(any()) wasNot called
        }
    }

    @Test
    fun `deleteBug should return delete requested bug`() {
        val id = UUID.randomUUID()

        every { bugRepository.deleteById(id) } just Runs

        bugService.deleteBug(id)

        verify(exactly = 1) { bugRepository.deleteById(id) }
    }
}
