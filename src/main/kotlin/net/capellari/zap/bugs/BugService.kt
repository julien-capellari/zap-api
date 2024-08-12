package net.capellari.zap.bugs

import net.capellari.zap.EmailService
import net.capellari.zap.Order
import net.capellari.zap.bugs.dtos.BugFiltersDto
import net.capellari.zap.bugs.dtos.BugRequestDto
import net.capellari.zap.bugs.dtos.BugResponseDto
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class BugService(
    private val bugRepository: BugRepository,
    private val emailService: EmailService
) {
    fun listBugs(filters: BugFiltersDto, order: Order): List<BugResponseDto> {
        val example = Example.of(
            Bug(filters),
            ExampleMatcher.matchingAll()
                .withIgnorePaths("id", "title", "date", "description")
                .let { if (filters.status == null) it.withIgnorePaths("status") else it }
                .let { if (filters.severity == null) it.withIgnorePaths("severity") else it },
        )

        return bugRepository.findAll(example, Sort.by(order.toDirection(), "date"))
            .map { BugResponseDto(it) }
    }

    fun getBug(id: UUID): BugResponseDto? {
        return bugRepository.findByIdOrNull(id)
            ?.let { BugResponseDto(it) }
    }

    fun createBug(data: BugRequestDto): BugResponseDto {
        return BugResponseDto(bugRepository.save(Bug(data)))
    }

    fun updateBug(id: UUID, update: BugRequestDto): BugResponseDto? {
        return (bugRepository.findByIdOrNull(id) ?: return null)
            .apply {
                if (status === BugStatus.TODO && update.status === BugStatus.VALIDATED) {
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot move a TODO bug to VALIDATED")
                }

                title = update.title
                date = update.date
                severity = update.severity
                status = update.status
                description = update.description
            }
            .let {
                BugResponseDto(bugRepository.save(it))
            }
            .also {
                emailService.sendEmail("admin@zap.net", "Bug status changed", "Bug ${it.title} changed to ${it.status} status")
            }
    }

    fun deleteBug(id: UUID) {
        bugRepository.deleteById(id)
    }
}
