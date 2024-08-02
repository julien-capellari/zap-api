package net.capellari.zap.bugs

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

    fun getBug(id: UUID): BugResponseDto
    ? {
        return this.bugRepository.findById(id)
            .map { BugResponseDto(it) }
            .getOrNull()
    }
}