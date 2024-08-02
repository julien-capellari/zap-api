package net.capellari.zap.bugs

import net.capellari.zap.bugs.dtos.BugCreateDto
import net.capellari.zap.bugs.dtos.BugResponseDto
import net.capellari.zap.bugs.dtos.BugUpdateDto
import org.springframework.stereotype.Service
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Service
class BugService(private val bugRepository: BugRepository) {
    fun listBugs(): List<BugResponseDto> {
        return this.bugRepository.findAll()
            .map { BugResponseDto(it) }
    }

    fun createBug(data: BugCreateDto): BugResponseDto {
        val bug = this.bugRepository.save(Bug(data))
        return BugResponseDto(bug)
    }

    fun getBug(id: UUID): BugResponseDto? {
        return this.bugRepository.findById(id)
            .map { BugResponseDto(it) }
            .getOrNull()
    }

    fun updateBug(id: UUID, update: BugUpdateDto): BugResponseDto? {
        val bug = this.bugRepository.findById(id).getOrNull() ?: return null
        val result = this.bugRepository.save(Bug(
            id = id,
            title = update.title ?: bug.title,
            date = update.date ?: bug.date,
            severity = update.severity ?: bug.severity,
            status = update.status ?: bug.status,
            description = update.description ?: bug.description,
        ))

        return BugResponseDto(result)
    }

    fun deleteBug(id: UUID) {
        this.bugRepository.deleteById(id)
    }
}