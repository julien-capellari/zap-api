package net.capellari.zap.bugs

import net.capellari.zap.bugs.dtos.BugRequestDto
import net.capellari.zap.bugs.dtos.BugResponseDto
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BugService(
    private val bugRepository: BugRepository,
) {
    fun listBugs(): List<BugResponseDto> {
        return bugRepository.findAll()
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
        return bugRepository.findByIdOrNull(id)
            ?.apply {
                title = update.title
                date = update.date
                severity = update.severity
                status = update.status
                description = update.description
            }
            ?.let {
                BugResponseDto(bugRepository.save(it))
            }
    }

    fun deleteBug(id: UUID) {
        bugRepository.deleteById(id)
    }
}
