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
        return this.bugRepository.findAll()
            .map { BugResponseDto(it) }
    }

    fun getBug(id: UUID): BugResponseDto? {
        return this.bugRepository.findByIdOrNull(id)
            ?.let { BugResponseDto(it) }
    }

    fun createBug(data: BugRequestDto): BugResponseDto {
        return this.bugRepository.save(Bug(data))
            .let { BugResponseDto(it) }
    }

    fun updateBug(id: UUID, update: BugRequestDto): BugResponseDto? {
        return this.bugRepository.findByIdOrNull(id)
            ?.apply {
                title = update.title
                date = update.date
                severity = update.severity
                status = update.status
                description = update.description
            }
            ?.let {
                BugResponseDto(this.bugRepository.save(it))
            }
    }

    fun deleteBug(id: UUID) {
        this.bugRepository.deleteById(id)
    }
}
