package net.capellari.zap.bugs

import net.capellari.zap.bugs.dtos.BugRequestDto
import net.capellari.zap.bugs.dtos.BugResponseDto
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class BugService(private val bugRepository: BugRepository) {
    fun listBugs(): List<BugResponseDto> {
        return this.bugRepository.findAll()
            .map { BugResponseDto(it) }
    }

    fun createBug(data: BugRequestDto): BugResponseDto {
        val bug = this.bugRepository.save(Bug(data))
        return BugResponseDto(bug)
    }

    fun getBug(id: UUID): BugResponseDto? {
        return this.bugRepository.findById(id)
            .map { BugResponseDto(it) }
            .getOrNull()
    }

    fun updateBug(id: UUID, update: BugRequestDto): BugResponseDto? {
        (this.bugRepository.findById(id).getOrNull() ?: return null)
            .apply {
                title = update.title
                date = update.date
                severity = update.severity
                status = update.status
                description = update.description
            }
            .let {
                val result = this.bugRepository.save(it)
                return BugResponseDto(result)
            }
    }

    fun deleteBug(id: UUID) {
        this.bugRepository.deleteById(id)
    }
}